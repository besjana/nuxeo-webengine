/*
 * (C) Copyright 2006-2010 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.ecm.webengine.jaxrs.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.jaxrs.session.impl.PerRequestCoreProvider;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class SessionFactory {

    public static final String SESSION_FACTORY_KEY = SessionFactory.class.getName();

    private static volatile String defaultRepository = "default";

    public static void setDefaultRepository(String repoName) {
        defaultRepository = repoName;
    }

    public static String getRepositoryName(HttpServletRequest request) {
        String v = request.getHeader("X-NXRepository");
        if (v == null) {
            v = request.getParameter("nxrepository");
        }
        return v != null ? v : defaultRepository;
    }

    public static CoreSessionProvider<?> getCoreProvider(HttpServletRequest request) {
        CoreSessionProvider<?> provider = (CoreSessionProvider<?>)request.getAttribute(SESSION_FACTORY_KEY);
        if (provider == null) {
            HttpSession s = request.getSession(false);
            if (s != null) {
                provider = (CoreSessionProvider<?>)s.getAttribute(SESSION_FACTORY_KEY);
            }
            if (provider == null) {
                provider = new PerRequestCoreProvider();
            }
            request.setAttribute(SESSION_FACTORY_KEY, provider);
        }
        return provider;
    }

    public static void dispose(HttpServletRequest request) {
        CoreSessionProvider<?> provider = (CoreSessionProvider<?>)request.getAttribute(SESSION_FACTORY_KEY);
        if (provider != null) {
            request.removeAttribute(SESSION_FACTORY_KEY);
            provider.onRequestDone(request);
        }
    }

    public static CoreSession getSession(HttpServletRequest request) {
        return getSession(request, getRepositoryName(request));
    }

    public static CoreSession getSession(HttpServletRequest request, String repositoryName) {
        return getCoreProvider(request).getSession(request, repositoryName);
    }

    public static SessionRef getSessionRef(HttpServletRequest request) {
        return getSessionRef(request, getRepositoryName(request));
    }

    public static SessionRef getSessionRef(HttpServletRequest request, String repositoryName) {
        return getCoreProvider(request).getSessionRef(request, repositoryName);
    }

}
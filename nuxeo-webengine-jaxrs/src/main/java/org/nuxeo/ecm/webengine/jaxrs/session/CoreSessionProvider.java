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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class CoreSessionProvider<REF extends SessionRef> {

    private static Log log = LogFactory.getLog(CoreSessionProvider.class);

    protected Map<String, REF> sessions;


    protected CoreSessionProvider() {
        this.sessions = new HashMap<String, REF>();
    }

    /**
     * The HTTP request was consumed. Do any request level cleanup now.
     * @param request
     */
    protected abstract void onRequestDone(HttpServletRequest request);

    protected abstract REF createSessionRef(CoreSession session);

    public SessionRef[] getSessions() {
        return sessions.values().toArray(new SessionRef[sessions.size()]);
    }

    public SessionRef getSessionRef(HttpServletRequest request, String repoName) {
        REF ref = sessions.get(repoName);
        if (ref == null) {
            ref = createSessionRef(createSession(request, repoName));
            sessions.put(repoName, ref);
        }
        return ref;
    }

    public CoreSession getSession(HttpServletRequest request,
            String repoName) {
        return getSessionRef(request, repoName).get();
    }

    protected CoreSession createSession(HttpServletRequest request, String repoName) {
        try {
            return _createSession(request, repoName);
        } catch (Exception e) {
            throw new WebApplicationException(e, 500);
        }
    }

    protected CoreSession _createSession(HttpServletRequest request, String repoName) throws Exception {
        if (request.getUserPrincipal() == null) {
            throw new java.lang.IllegalStateException("Not authenticated user is trying to get a core session");
        }
        RepositoryManager rm = Framework.getService(RepositoryManager.class);
        Repository repo = null;
        if (repoName == null) {
            repo = rm.getDefaultRepository();
        } else {
            repo = rm.getRepository(repoName);
        }
        if (repo == null) {
            //TODO use custom exception
            throw new java.lang.IllegalStateException("Unable to get " + repoName
                    + " repository");
        }
        return repo.open();
    }

    public boolean hasSessions() {
        return !sessions.isEmpty();
    }

    protected void destroy() {
        for (SessionRef ref : getSessions()) {
            try {
                ref.destroy();
            } catch(Throwable t) {
                log.error("Failed to destroy core session", t);
            }
        }
        sessions = null;
    }

}
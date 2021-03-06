/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *
 * $Id$
 */

package org.nuxeo.ecm.webengine.security.guards;

import java.util.Collection;

import org.nuxeo.ecm.webengine.security.Guard;
import org.nuxeo.runtime.model.Adaptable;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class Or implements Guard {

    protected Guard[] perms;


    public Or(Collection<Guard> guards) {
        this(guards.toArray(new Guard[guards.size()]));
    }

    public Or(Guard ... guards) {
        if (guards == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        perms = guards;
    }

    public boolean check(Adaptable context) {
        for (Guard perm : perms) {
            if (perm.check(context)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (perms == null) {
            return " [OR] ";
        }
        buf.append('(').append(perms[0]);
        for (int i=1; i<perms.length; i++) {
            buf.append(" OR ").append(perms[i]);
        }
        return buf.append(')').toString();
    }
}

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
package org.nuxeo.ecm.webengine.jaxrs.servlet.config;

import java.util.HashMap;

import javax.servlet.Filter;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.webengine.jaxrs.BundleNotFoundException;
import org.nuxeo.ecm.webengine.jaxrs.Utils;
import org.nuxeo.ecm.webengine.jaxrs.Utils.ClassRef;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@XObject("filter")
public class FilterDescriptor {

    @XNode("@class")
    protected String classRef;

    @XNodeMap(value="property", key="@name", type=HashMap.class, componentType=String.class, trim=true, nullByDefault=false)
    protected HashMap<String, String> initParams;

    private ClassRef ref;

    public ClassRef getClassRef() throws ClassNotFoundException, BundleNotFoundException {
        if (ref == null) {
            ref = Utils.getClassRef(classRef);
        }
        return ref;
    }

    public String getRawClassRef() {
        return classRef;
    }

    public Filter getFilter() throws Exception {
        return (Filter)getClassRef().get().newInstance();
    }

    public HashMap<String, String> getInitParams() {
        return initParams;
    }

    @Override
    public String toString() {
        return classRef+" "+initParams;
    }
}
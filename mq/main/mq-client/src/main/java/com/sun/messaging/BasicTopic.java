/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2000-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/*
 * @(#)BasicTopic.java	1.11 06/28/07
 */ 

package com.sun.messaging;

import javax.jms.*;

/**
 * A <code>BasicTopic</code> represents an identity of a repository of messages
 * used in the JMS Publish/Subscribe messaging domain.
 *
 * @see         javax.jms.Topic javax.jms.Topic
 */
public class BasicTopic extends com.sun.messaging.Destination implements javax.jms.Topic {

    /**
     * Constructs an identity of a Publish/Subscribe Topic with the default name
     */
    public BasicTopic () {
        super();
    }

    /**
     * Constructs an identity of a Publish/Subscribe Topic with the given name
     *
     * @param   name The name of the Topic
     */
    public BasicTopic (String name) throws javax.jms.JMSException {
        super(name);
    }

    /**
     * Compares this Topic to the specified object.
     * The result is <code>true</code> if and only if the arguement is not
     * <code>null</code> and is a <code>Topic</code> object with the same
     * Topic Name as this object.
     *
     * @param   anObject  The object to compare this <code>Topic</code> against.
     * @return  <code>true</code> if the object and this <code>Topic</code>are equal;
     *          <code>false</code> otherwise.
     *
     */
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if ((anObject != null) && (anObject instanceof BasicTopic)) {
            try {
                //null test - since getTopicName could also return null
                String name = getTopicName();
                if (name != null) {
                    return name.equals(((BasicTopic)anObject).getTopicName());
                } else {
                    return (name == ((BasicTopic)anObject).getTopicName()) ;
                }
            } catch(JMSException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        String name = null;
        try {
            name = getTopicName();
        } catch (Exception ex) {
        }
        if (name == null) return super.hashCode();
        return name.hashCode();
    }


    /**
     * Returns whether this is a Queueing type of Destination object
     * 
     * @return whether this is a Queueing type of Destination object
     */
    public boolean isQueue() {
        return false;
    }

    /**
     * Returns whether this is a Temporary type of Destination object
     * 
     * @return whether this is a Temporary type of Destination object
     */
    public boolean isTemporary() {
        return false;
    }
}

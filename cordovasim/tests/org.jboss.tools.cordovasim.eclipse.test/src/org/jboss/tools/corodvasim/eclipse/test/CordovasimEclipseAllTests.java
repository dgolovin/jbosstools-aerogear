/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.corodvasim.eclipse.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 * @author Ilya Buziuk (ibuziuk)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
JettyBundlesTest.class,
JettyServerTest.class
})
public class CordovasimEclipseAllTests{
}

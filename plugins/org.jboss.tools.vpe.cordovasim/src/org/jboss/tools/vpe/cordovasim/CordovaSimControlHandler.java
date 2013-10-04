/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.cordovasim;
import org.eclipse.swt.browser.Browser;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.ui.BrowserSimControlHandler;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class CordovaSimControlHandler extends BrowserSimControlHandler {

	public CordovaSimControlHandler(Browser browser, String homeUrl, SpecificPreferences specificPreferences) {
		super(browser, homeUrl, specificPreferences);
	}

	@Override
	public void goBack() {
		if (!isBackButtonProcessed()) {
			super.goBack(); 
		}
	}
	
	@Override
	public void goHome() { 
		if (!isHomeButtonProcessed()) {
			super.goHome(); 
		}
	}
	
	
	/**
	 * @return {@link Boolean} that indicates whether device's backButton was overridden via PhoneGap's Event API 
	 */
	private boolean isBackButtonProcessed() { 
		return (Boolean) browser.evaluate("return !!window.opener.bsBackbuttonPressed && !window.opener.bsBackbuttonPressed()");
	}
	
	/**
	 * This method will return true only when inAppBrowser is shown. 
	 * Moreover it's impossible to override homeButton for Android 4.0. 
	 * Pressing home button when inAppBrowser is shown will simply close it  
	 * 
	 * @return {@link Boolean} that indicates whether device's homeButton was overridden 
	 */
	private boolean isHomeButtonProcessed() { // XXX 
		return (Boolean) browser.evaluate("return !!window.opener.overrideHomeButtonForInAppBrowser && !!window.opener.bsBackbuttonPressed && !window.opener.bsBackbuttonPressed()");
	}
}

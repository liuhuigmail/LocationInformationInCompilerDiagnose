package com.application.areca.adapters;

import com.application.areca.plugins.FileSystemPolicyXMLHandler;

/**
 * Base implementation for all FileSystemPolicyXMLHandler, used for serialization of FileSystemPolicy instances
 * <BR>
 * @author Olivier PETRUCCI
 * <BR>
 *
 */

 /*
 Copyright 2005-2014, Olivier PETRUCCI.

This file is part of Areca.  

    Areca is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Areca is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Areca; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 */
public abstract class AbstractFileSystemPolicyXMLHandler 
implements FileSystemPolicyXMLHandler, XMLTags  {

	protected int version = 1;

    public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	protected static String buildDeprecatedArchiveName(String prefix, boolean isOverwrite) {
		if (isOverwrite) {
			return prefix;
		} else {
			return prefix + "_%YYYY%%MM%%DD%_%hh%%mm%";
		}
	}
}

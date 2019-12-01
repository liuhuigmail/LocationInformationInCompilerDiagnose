package com.application.areca;

import java.util.GregorianCalendar;

import com.application.areca.context.ProcessContext;
import com.application.areca.impl.copypolicy.AbstractCopyPolicy;
import com.application.areca.indicator.IndicatorMap;
import com.application.areca.metadata.manifest.Manifest;
import com.application.areca.metadata.transaction.TransactionHandler;
import com.application.areca.metadata.transaction.TransactionPoint;
import com.application.areca.version.VersionInfos;
import com.myJava.util.log.Logger;
import com.myJava.util.xml.AdapterException;

/**
 * 
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
public class ActionProxy {

	/**
	 * Launch a simulation on a target
	 */
	public static SimulationResult processSimulateOnTarget(AbstractTarget target, ProcessContext context) throws ApplicationException {
		return target.processSimulate(context);
	}

	/**
	 * Compute indicators for a target
	 */
	public static IndicatorMap processIndicatorsOnTarget(AbstractTarget target, ProcessContext context) throws ApplicationException {
		return target.computeIndicators(context);
	}

	/**
	 * Launch a check on a target
	 */
	public static void processCheckOnTarget(
			AbstractTarget target, 
			CheckParameters checkParams,
			GregorianCalendar date, 
			ProcessContext context
	) throws ApplicationException {
		LogHelper.logTarget(target);
		target.processArchiveCheck(checkParams, date, null, true, context);
	}

	/**
	 * Launch a recovery on a target
	 */
	public static void processRecoverOnTarget(
			AbstractTarget target, 
			ArecaRawFileList filters, 
			AbstractCopyPolicy policy,
			String path, 
			boolean appendSuffix,
			GregorianCalendar date, 
			boolean keepDeletedEntries,
			boolean checkRecoveredEntries, 
			ProcessContext context
	) throws ApplicationException {
		LogHelper.logTarget(target);
		target.processRecover(path, appendSuffix, filters, policy, date, keepDeletedEntries, checkRecoveredEntries, context);
	}

	public static void processRecoverOnTarget(
			AbstractTarget target, 
			String path, 
			GregorianCalendar date, 
			String entry, 
			AbstractCopyPolicy policy,
			boolean checkRecoveredEntries, 
			ProcessContext context
	) throws ApplicationException {
		LogHelper.logTarget(target);
		target.processRecover(path, date, entry, policy, checkRecoveredEntries, context);
	}

	/**
	 * Launch a merge on a target
	 */
	public static void processMergeOnTarget(
			AbstractTarget target, 
			GregorianCalendar fromDate, 
			GregorianCalendar toDate, 
			Manifest manifest, 
			MergeParameters params, 
			CheckParameters checkParams,
			ProcessContext context
	) throws ApplicationException {  
		target.processMerge(fromDate, toDate, manifest, params, checkParams, true, context);
	}  

	public static void processMergeOnTarget(
			AbstractTarget target, 
			int fromDelay, 
			int toDelay, 
			Manifest manifest, 
			MergeParameters params, 
			CheckParameters checkParams,
			ProcessContext context
	) throws ApplicationException {
		LogHelper.logTarget(target);
		LogHelper.logTarget(target);
		target.processMerge(fromDelay, toDelay, manifest, params, checkParams, true, context);
	}  

	public static void processMergeOnTargetImpl(
			AbstractTarget target, 
			int fromDelay, 
			int toDelay, 
			Manifest manifest, 
			MergeParameters params, 
			CheckParameters checkParams,
			boolean runProcessors,
			ProcessContext context
	) throws ApplicationException { 
		LogHelper.logTarget(target);
		target.processMerge(fromDelay, toDelay, manifest, params, checkParams, runProcessors, context);
	}

	/**
	 * Deletes archives for a target
	 */
	public static void processDeleteOnTarget(AbstractTarget target, int delay, ProcessContext context) throws ApplicationException {
		LogHelper.logTarget(target);
		target.processDeleteArchives(delay, context);
	}  

	public static void processDeleteOnTarget(AbstractTarget target, GregorianCalendar fromDate, ProcessContext context) throws ApplicationException {   
		LogHelper.logTarget(target);
		target.processDeleteArchives(fromDate, context);
	}  
}

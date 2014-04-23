/*
 * Copyright (C) 2014 Dell, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dell.doradus.service.spider;

import com.dell.doradus.common.ApplicationDefinition;
import com.dell.doradus.common.HttpCode;
import com.dell.doradus.common.RESTResponse;
import com.dell.doradus.common.TableDefinition;
import com.dell.doradus.common.Utils;
import com.dell.doradus.common.ScheduleDefinition.SchedType;
import com.dell.doradus.service.StorageService;
import com.dell.doradus.service.rest.NotFoundException;
import com.dell.doradus.service.rest.RESTCallback;
import com.dell.doradus.service.schema.SchemaService;
import com.dell.doradus.service.taskmanager.TaskManagerService;

/**
 * Processes the following REST commands:
 * <pre>
 *      DELETE /_tasks/{application}/{table}/{task-type}
 *      DELETE /_tasks/{application}/{table}/{task-type}/{field}
 * </pre>
 */
public class StopFixDataCmd extends RESTCallback {

	@Override
	protected RESTResponse invoke() {
        String application = m_request.getVariableDecoded("application");
        ApplicationDefinition appDef = SchemaService.instance().getApplication(application);
        if (appDef == null) {
            throw new NotFoundException("Unknown application: " + application);
        }
        
        StorageService storageService = SchemaService.instance().getStorageService(appDef);
        Utils.require(storageService.getClass().getSimpleName().equals(SpiderService.class.getSimpleName()),
                      "Only SpiderService applications support statistics: %s", application);
        
        String table = m_request.getVariableDecoded("table");
        TableDefinition tableDef = appDef.getTableDef(table);
        Utils.require(tableDef != null, "Unknown table for application '%s': %s", application, table);

        String taskType = m_request.getVariableDecoded("task-type");
        SchedType schedType = SchedType.getByName(taskType);
        Utils.require(schedType != null, "Unrecognized task type: %s", taskType);
        Utils.require(
        		schedType == SchedType.DELETE_LINK ||
        		schedType == SchedType.DELETE_SCALAR ||
        		schedType == SchedType.RE_INDEX,
        		"Tasks of the %s type cannot be stopped", taskType);
        
        String field = m_request.getVariableDecoded("field");
        if (field == null) field = "*";
        
        String taskId = table + "/" + taskType + "/" + field;
        
        if (TaskManagerService.instance().interrupt(application, taskId)) {
            return new RESTResponse(HttpCode.OK, "Task(s) interrupted");
        } else {
        	return new RESTResponse(HttpCode.NOT_FOUND,
        			String.format("No running tasks %s were found to stop", taskId));
        }
	}

}

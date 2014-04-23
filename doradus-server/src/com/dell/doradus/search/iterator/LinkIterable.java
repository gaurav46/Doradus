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

package com.dell.doradus.search.iterator;

import java.util.Iterator;
import java.util.List;

import com.dell.doradus.common.FieldDefinition;
import com.dell.doradus.core.ObjectID;

public class LinkIterable implements Iterable<ObjectID> {
	private FieldDefinition m_link;
	private Integer m_shard;
	private ObjectID m_key;
	private int m_bufferSize;
	private List<ObjectID> m_buffer;
	
    public LinkIterable(FieldDefinition link, Integer shard, ObjectID key, int bufferSize, List<ObjectID> buffer) {
    	m_link = link;
    	m_shard = shard;
    	m_key = key;
    	m_bufferSize = bufferSize;
    	m_buffer = buffer;
    }
    
	@Override public Iterator<ObjectID> iterator() {
		if(m_buffer.size() == 0) return NoneIterator.instance;
		else if(m_buffer.size() < m_bufferSize) return m_buffer.iterator();
		else return new LinkIterator(m_link, m_shard, m_key, m_bufferSize, m_buffer);
	}
}

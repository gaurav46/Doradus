package com.dell.doradus.logservice.search.filter;

import java.util.ArrayList;
import java.util.List;

import com.dell.doradus.logservice.ChunkReader;

public class FilterOr implements IFilter {
    private List<IFilter> m_filters = new ArrayList<>();
    
    public FilterOr() {}

    public FilterOr(IFilter... filters) {
        for(IFilter filter: filters) {
            m_filters.add(filter);
        }
    }
    
    public void add(IFilter filter) {
        m_filters.add(filter);
    }
    
    @Override public boolean check(ChunkReader reader, int doc) {
        for(IFilter filter: m_filters) {
            if(filter.check(reader, doc)) return true;
        }
        return false;
    }
}

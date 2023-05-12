package tech.filatov.pigeoner.dto;

import java.util.List;

public interface HierarchicalDto<T extends HierarchicalDto<T>> {
    Long getId();
    Long getParentId();
    List<T> getChildren();
}

package org.project.lomfx.inserters;

import org.project.lomfx.annotations.Modifier;

import javax.lang.model.element.Element;

public interface MethodInserter {

    void insert(Element element, String name, Modifier modifier, boolean readOnly);

}

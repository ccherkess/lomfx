package org.project.lomfx.inserters;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import org.project.lomfx.annotations.Modifier;
import org.project.lomfx.utils.FxValueMethodsNameGenerator;

import javax.lang.model.element.Element;

public class PropertyMethodInserter extends AbstractMethodInserter {

    public PropertyMethodInserter(JavacProcessingEnvironment jcProcEnv) {
        super(jcProcEnv);
    }

    @Override
    public void insert(Element element, String name, Modifier modifier, boolean readOnly) {
        JCTree.JCVariableDecl varDecl = getVariableDecl(element);
        long flags = getFlags(element, modifier);

        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(
                treeMaker.Modifiers(flags),
                names.fromString(FxValueMethodsNameGenerator.defaultPropertyName(name)),
                varDecl.vartype,
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(
                        0,
                        List.of(
                                treeMaker.Return(treeMaker.Ident(varDecl))
                        )
                ),
                null
        );

        super.insert(methodDecl, getClassDecl(element));
    }

}

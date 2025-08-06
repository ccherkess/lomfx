package org.project.lomfx.inserters;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import org.project.lomfx.annotations.Modifier;
import org.project.lomfx.utils.FxValueMethodsNameGenerator;
import org.project.lomfx.utils.FxValueTypeGetter;

import javax.lang.model.element.Element;

public class GetterMethodInserter extends AbstractMethodInserter {

    public GetterMethodInserter(JavacProcessingEnvironment jcProcEnv) {
        super(jcProcEnv);
    }

    public void insert(Element element, String name, Modifier modifier, boolean readOnly) {
        JCTree.JCVariableDecl varDecl = getVariableDecl(element);
        long flags = getFlags(element, modifier);
        JCTree.JCExpression returnType = FxValueTypeGetter.getType(varDecl, treeMaker);

        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(
                treeMaker.Modifiers(flags),
                names.fromString(FxValueMethodsNameGenerator.defaultGetterName(name)),
                returnType,
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(
                        0,
                        List.of(getReturnStatement(varDecl))
                ),
                null
        );

        super.insert(methodDecl, getClassDecl(element));
    }

    private JCTree.JCReturn getReturnStatement(JCTree.JCVariableDecl varDecl) {
        return treeMaker.Return(
                treeMaker.Apply(
                        List.nil(),
                        treeMaker.Select(treeMaker.Ident(varDecl), names.fromString("getValue")),
                        List.nil()
                )
        );
    }

}

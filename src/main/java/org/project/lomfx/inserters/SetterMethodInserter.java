package org.project.lomfx.inserters;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import org.project.lomfx.annotations.Modifier;
import org.project.lomfx.utils.FxValueMethodsNameGenerator;
import org.project.lomfx.utils.FxValueTypeGetter;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class SetterMethodInserter extends AbstractMethodInserter {

    public SetterMethodInserter(JavacProcessingEnvironment jcProcEnv) {
        super(jcProcEnv);
    }

    @Override
    public void insert(Element element, String name, Modifier modifier, boolean readOnly) {
        if (readOnly) {
            return;
        }

        JCTree.JCVariableDecl varDecl = getVariableDecl(element);
        long flags = getFlags(element, modifier);
        JCTree.JCExpression parameterType = FxValueTypeGetter.getType(varDecl, treeMaker);

        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(
                treeMaker.Modifiers(flags),
                names.fromString(FxValueMethodsNameGenerator.defaultSetterName(name)),
                treeMaker.TypeIdent(TypeTag.VOID),
                List.nil(),
                List.of(
                        treeMaker.VarDef(
                                treeMaker.Modifiers(Flags.PARAMETER),
                                names.fromString("value"),
                                parameterType,
                                null
                        )
                ),
                List.nil(),
                treeMaker.Block(
                        0,
                        List.of(
                                treeMaker.Exec(
                                        treeMaker.Apply(
                                                List.nil(),
                                                treeMaker.Select(treeMaker.Ident(varDecl), names.fromString("set")),
                                                List.of(treeMaker.Ident(names.fromString("value")))
                                        )
                                )
                        )
                ),
                null
        );

        messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, methodDecl.toString());

        super.insert(methodDecl, getClassDecl(element));
    }

}

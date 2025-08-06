package org.project.lomfx.utils;

import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;

public class FxValueTypeGetter {

    public static JCTree.JCExpression getType(JCTree.JCVariableDecl varDecl, TreeMaker treeMaker) {
        if (varDecl.vartype instanceof JCTree.JCTypeApply typeApply) {
            return typeApply.arguments.get(0);
        } else {
            String typeName = varDecl.vartype.toString().replace("Property", "");

            TypeTag typeTag = switch (typeName) {
                case "Boolean" -> TypeTag.BOOLEAN;
                case "Double" -> TypeTag.DOUBLE;
                case "Float" -> TypeTag.FLOAT;
                case "Integer" -> TypeTag.INT;
                case "Long" -> TypeTag.LONG;
                default -> throw new IllegalArgumentException("Unknown wrapper type: " + varDecl.vartype.toString());
            };

            return treeMaker.TypeIdent(typeTag);
        }
    }

}

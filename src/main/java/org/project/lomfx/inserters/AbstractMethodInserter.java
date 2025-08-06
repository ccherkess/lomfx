package org.project.lomfx.inserters;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import org.project.lomfx.annotations.Modifier;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.sun.tools.javac.code.Flags.STATIC;

abstract class AbstractMethodInserter implements MethodInserter {

    protected final TreeMaker treeMaker;
    protected final Names names;
    protected final MemberEnter memberEnterInst;
    protected final Enter enter;
    protected final Method memberEnterMeth;
    protected final Trees trees;
    protected final Messager messager;

    protected AbstractMethodInserter(JavacProcessingEnvironment jcProcEnv) {
        Context context = jcProcEnv.getContext();
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context);
        memberEnterInst = MemberEnter.instance(context);
        enter = Enter.instance(context);
        trees = Trees.instance(jcProcEnv);
        messager = jcProcEnv.getMessager();

        try {
            memberEnterMeth = MemberEnter.class.getDeclaredMethod("memberEnter", JCTree.class, Env.class);
            memberEnterMeth.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void insert(JCTree tree, JCTree.JCClassDecl parent) {
        parent.defs = parent.defs.append(tree);

        try {
            memberEnterMeth.invoke(memberEnterInst, tree, enter.getEnv(parent.sym));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected JCTree.JCClassDecl getClassDecl(Element element) {
        return (JCTree.JCClassDecl) trees.getPath(element).getParentPath().getLeaf();
    }

    protected JCTree.JCVariableDecl getVariableDecl(Element element) {
        return (JCTree.JCVariableDecl) trees.getPath(element).getLeaf();
    }

    protected long getFlags(Element element, Modifier modifier) {
        long flags = switch (modifier) {
            case PUBLIC -> Flags.PUBLIC;
            case PROTECTED -> Flags.PROTECTED;
            case PACKAGE -> 0;
            case PRIVATE -> Flags.PRIVATE;
        };

        if ((getVariableDecl(element).mods.flags & (long) STATIC) != 0) {
            flags = flags | STATIC;
        }

        return flags;
    }

}

////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2016 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.annotation;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CommonUtils;

/**
 * Check location of annotation on language elements.
 * By default, Check enforce to locate annotations immediately after
 * documentation block and before target element, annotation should be located
 * on separate line from target element.
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * &#64;Override
 * &#64;Nullable
 * public String getNameIfPresent() { ... }
 * </pre>
 *
 * <p>
 * Check have following options:
 * </p>
 * <ul>
 * <li>allowSamelineMultipleAnnotations - to allow annotation to be located on
 * the same line as target element. Default value is false.
 * </li>
 *
 * <li>
 * allowSamelineSingleParameterlessAnnotation - to allow single parameterless
 * annotation to be located on the same line as target element. Default value is false.
 * </li>
 *
 * <li>
 * allowSamelineParameterizedAnnotation - to allow parameterized annotation
 * to be located on the same line as target element. Default value is false.
 * </li>
 * </ul>
 * <br>
 * <p>
 * Example to allow single parameterless annotation on the same line:
 * </p>
 * <pre>
 * &#64;Override public int hashCode() { ... }
 * </pre>
 *
 * <p>Use following configuration:
 * <pre>
 * &lt;module name=&quot;AnnotationLocation&quot;&gt;
 *    &lt;property name=&quot;allowSamelineMultipleAnnotations&quot; value=&quot;false&quot;/&gt;
 *    &lt;property name=&quot;allowSamelineSingleParameterlessAnnotation&quot;
 *    value=&quot;true&quot;/&gt;
 *    &lt;property name=&quot;allowSamelineParameterizedAnnotation&quot; value=&quot;false&quot;
 *    /&gt;
 * &lt;/module&gt;
 * </pre>
 * <br>
 * <p>
 * Example to allow multiple parameterized annotations on the same line:
 * </p>
 * <pre>
 * &#64;SuppressWarnings("deprecation") &#64;Mock DataLoader loader;
 * </pre>
 *
 * <p>Use following configuration:
 * <pre>
 * &lt;module name=&quot;AnnotationLocation&quot;&gt;
 *    &lt;property name=&quot;allowSamelineMultipleAnnotations&quot; value=&quot;true&quot;/&gt;
 *    &lt;property name=&quot;allowSamelineSingleParameterlessAnnotation&quot;
 *    value=&quot;true&quot;/&gt;
 *    &lt;property name=&quot;allowSamelineParameterizedAnnotation&quot; value=&quot;true&quot;
 *    /&gt;
 * &lt;/module&gt;
 * </pre>
 * <br>
 * <p>
 * Example to allow multiple parameterless annotations on the same line:
 * </p>
 * <pre>
 * &#64;Partial &#64;Mock DataLoader loader;
 * </pre>
 *
 * <p>Use following configuration:
 * <pre>
 * &lt;module name=&quot;AnnotationLocation&quot;&gt;
 *    &lt;property name=&quot;allowSamelineMultipleAnnotations&quot; value=&quot;true&quot;/&gt;
 *    &lt;property name=&quot;allowSamelineSingleParameterlessAnnotation&quot;
 *    value=&quot;true&quot;/&gt;
 *    &lt;property name=&quot;allowSamelineParameterizedAnnotation&quot; value=&quot;false&quot;
 *    /&gt;
 * &lt;/module&gt;
 * </pre>
 *
 * @author maxvetrenko
 */
public class AnnotationLocationCheck extends AbstractCheck {
    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY_ANNOTATION_LOCATION_ALONE = "annotation.location.alone";

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY_ANNOTATION_LOCATION = "annotation.location";

    /**
     * If true, it allows single prameterless annotation to be located on the same line as
     * target element.
     */
    private boolean allowSamelineSingleParameterlessAnnotation = true;

    /**
     * If true, it allows parameterized annotation to be located on the same line as
     * target element.
     */
    private boolean allowSamelineParameterizedAnnotation;

    /**
     * If true, it allows annotation to be located on the same line as
     * target element.
     */
    private boolean allowSamelineMultipleAnnotations;

    /**
     * Sets if allow same line single parameterless annotation.
     * @param allow User's value of allowSamelineSingleParameterlessAnnotation.
     */
    public final void setAllowSamelineSingleParameterlessAnnotation(boolean allow) {
        allowSamelineSingleParameterlessAnnotation = allow;
    }

    /**
     * Sets if allow parameterized annotation to be located on the same line as
     * target element.
     * @param allow User's value of allowSamelineParameterizedAnnotation.
     */
    public final void setAllowSamelineParameterizedAnnotation(boolean allow) {
        allowSamelineParameterizedAnnotation = allow;
    }

    /**
     * Sets if allow annotation to be located on the same line as
     * target element.
     * @param allow User's value of allowSamelineMultipleAnnotations.
     */
    public final void setAllowSamelineMultipleAnnotations(boolean allow) {
        allowSamelineMultipleAnnotations = allow;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[] {
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ENUM_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
            TokenTypes.VARIABLE_DEF,
        };
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[] {
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ENUM_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
            TokenTypes.VARIABLE_DEF,
            TokenTypes.PARAMETER_DEF,
            TokenTypes.ANNOTATION_DEF,
            TokenTypes.TYPECAST,
            TokenTypes.LITERAL_THROWS,
            TokenTypes.IMPLEMENTS_CLAUSE,
            TokenTypes.TYPE_ARGUMENT,
            TokenTypes.LITERAL_NEW,
            TokenTypes.DOT,
            TokenTypes.ANNOTATION_FIELD_DEF,
        };
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtils.EMPTY_INT_ARRAY;
    }

    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST modifiersNode = ast.findFirstToken(TokenTypes.MODIFIERS);

        if (hasAnnotations(modifiersNode)) {
            checkAnnotations(modifiersNode, getAnnotationLevel(modifiersNode));
        }
    }

    /**
     * Some javadoc.
     * @param modifierNode Some javadoc.
     * @return Some javadoc.
     */
    private static boolean hasAnnotations(DetailAST modifierNode) {
        return modifierNode != null && modifierNode.findFirstToken(TokenTypes.ANNOTATION) != null;
    }

    /**
     * Some javadoc.
     * @param modifierNode Some javadoc.
     * @return Some javadoc.
     */
    private static int getAnnotationLevel(DetailAST modifierNode) {
        return modifierNode.getParent().getColumnNo();
    }

    /**
     * Explores the given node and all it's siblings and verifies location for each
     * discovered annotation node.
     * @param modifierNode node to check.
     * @param correctLevel expected indentation level.
     */
    private void checkAnnotations(DetailAST modifierNode, int correctLevel) {
        DetailAST node = modifierNode.getFirstChild();

        while (node != null) {
            if (node.getType() == TokenTypes.ANNOTATION) {
                final boolean hasParameters = isParameterized(node);

                if (!isCorrectLocation(node, hasParameters)) {
                    log(node.getLineNo(),
                            MSG_KEY_ANNOTATION_LOCATION_ALONE, getAnnotationName(node));
                }
                else if (node.getColumnNo() != correctLevel && !hasNodeBefore(node)) {
                    log(node.getLineNo(), MSG_KEY_ANNOTATION_LOCATION,
                            getAnnotationName(node), node.getColumnNo(), correctLevel);
                }
            }
            node = node.getNextSibling();
        }
    }

    /**
     * Some javadoc.
     * @param annotation Some javadoc.
     * @return Some javadoc.
     */
    private static boolean isParameterized(DetailAST annotation) {
        return annotation.findFirstToken(TokenTypes.EXPR) != null;
    }

    /**
     * Some javadoc.
     * @param annotation Some javadoc.
     * @return Some javadoc.
     */
    private static String getAnnotationName(DetailAST annotation) {
        DetailAST identNode = annotation.findFirstToken(TokenTypes.IDENT);
        if (identNode == null) {
            identNode = annotation.findFirstToken(TokenTypes.DOT).findFirstToken(TokenTypes.IDENT);
        }
        return identNode.getText();
    }

    /**
     * Some javadoc.
     * @param annotation Some javadoc.
     * @param hasParams Some javadoc.
     * @return Some javadoc.
     */
    private boolean isCorrectLocation(DetailAST annotation, boolean hasParams) {
        final boolean allowingCondition;

        if (hasParams) {
            allowingCondition = allowSamelineParameterizedAnnotation;
        }
        else {
            allowingCondition = allowSamelineSingleParameterlessAnnotation;
        }
        return allowSamelineMultipleAnnotations
            || allowingCondition && !hasNodeBefore(annotation)
            || !allowingCondition && !hasNodeBeside(annotation);
    }

    /**
     * Some javadoc.
     * @param annotation Some javadoc.
     * @return Some javadoc.
     */
    private static boolean hasNodeBefore(DetailAST annotation) {
        final int annotationLineNo = annotation.getLineNo();
        final DetailAST previousNode = annotation.getPreviousSibling();

        return previousNode != null && annotationLineNo == previousNode.getLineNo();
    }

    /**
     * Some javadoc.
     * @param annotation Some javadoc.
     * @return Some javadoc.
     */
    private static boolean hasNodeBeside(DetailAST annotation) {
        return hasNodeBefore(annotation) || hasNodeAfter(annotation);
    }

    /**
     * Some javadoc.
     * @param annotation Some javadoc.
     * @return Some javadoc.
     */
    private static boolean hasNodeAfter(DetailAST annotation) {
        final int annotationLineNo = annotation.getLineNo();
        DetailAST nextNode = annotation.getNextSibling();

        if (nextNode == null) {
            nextNode = annotation.getParent().getNextSibling();
        }

        return annotationLineNo == nextNode.getLineNo();
    }
}

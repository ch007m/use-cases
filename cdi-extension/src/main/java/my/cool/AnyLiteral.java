package my.cool;

import javax.enterprise.inject.Any;
import javax.enterprise.util.AnnotationLiteral;

final class AnyLiteral extends AnnotationLiteral<Any> implements Any {

    private static final long serialVersionUID = 1L;

    static final Any INSTANCE = new AnyLiteral();

    private AnyLiteral() {
    }
}

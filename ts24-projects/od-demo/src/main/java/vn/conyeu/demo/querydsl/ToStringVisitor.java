package vn.conyeu.demo.querydsl;

import com.querydsl.core.types.Templates;

public class ToStringVisitor implements Visitor<String, Templates> {
    public static final ToStringVisitor DEFAULT = new ToStringVisitor();

    private ToStringVisitor() { }

    @Override
    public String visit(Constant e, Templates templates) {
       throw new UnsupportedOperationException();
    }

    @Override
    public String visit(Operation<?> expr, Templates context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(Path<?> expr, Templates context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(TemplateExpression expr, Templates context) {
        throw new UnsupportedOperationException();
    }
}
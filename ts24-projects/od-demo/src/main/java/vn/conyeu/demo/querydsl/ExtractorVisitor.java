package vn.conyeu.demo.querydsl;

public class ExtractorVisitor implements Visitor<Expression<?>, Void> {
    public static final ExtractorVisitor DEFAULT = new ExtractorVisitor();

    private ExtractorVisitor() { }

    @Override
    public Expression<?> visit(Constant expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(Operation<?> expr, Void context) {
        return expr;
    }


    @Override
    public Expression<?> visit(Path<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(TemplateExpression expr, Void context) {
        return expr;
    }
}
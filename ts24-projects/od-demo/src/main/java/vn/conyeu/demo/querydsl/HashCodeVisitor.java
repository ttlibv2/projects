package vn.conyeu.demo.querydsl;

public class HashCodeVisitor implements Visitor<Integer,Void> {
    public static final HashCodeVisitor DEFAULT = new HashCodeVisitor();

    private HashCodeVisitor() { }


    @Override
    public Integer visit(Constant expr, Void context) {
        return expr.getConstant().hashCode();
    }

    @Override
    public Integer visit(Operation<?> expr, Void context) {
        int result = expr.getOperator().name().hashCode();
        return 31 * result + expr.getArgs().hashCode();
    }

    @Override
    public Integer visit(Path<?> expr, Void context) {
        return expr.getMetadata().hashCode();
    }

    @Override
    public Integer visit(TemplateExpression expr, Void context) {
        int result = expr.getTemplate().hashCode();
        return 31 * result + expr.getArgs().hashCode();
    }
}
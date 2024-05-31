package vn.conyeu.common.context;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class DbNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
    public static final String TABLE_NAME_PREFIX = "cy_odoo_";

    @Override
    public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment context) {
        Identifier identifier = new Identifier(TABLE_NAME_PREFIX + logicalName.getText(), false);
        return super.toPhysicalTableName(identifier, context);
    }
}
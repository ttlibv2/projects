//public class Operator {
//	EQ("="),
//	N_EQ("!="),
//	GT(">"),
//	GE(">="),
//	LT("<"),
//	LE("<="),
//	NONE("=?"), // unset or equals to (returns true if value is either None or False, otherwise behaves like =)
//	EQ_LIKE("=like"),  // matches field_name against the value pattern. An underscore _ in the pattern stands for (matches) any single character; a percent sign % matches any string of zero or more characters.
//	LIKE("like"), // matches field_name against the %value% pattern. Similar to =like but wraps value with '%' before matching
//	NOT_LIKE("not like"), // doesn't match against the %value% pattern
//	ILIKE("ilike"), // case insensitive like
//	NOT_ILIKE("not ilike"), // case insensitive not like
//	EQ_ILIKE("=ilike"), // case insensitive =like
//	IN("in"), //      is equal to any of the items from value, value should be a list of items
//	NOT_IN("not in"), //   is unequal to all of the items from value
//	CHILD_OF("child_of"), //  is a child (descendant) of a value record. Takes the semantics of the model into account (i.e following the relationship field named by _parent_name).
//	AND("&"), //logical AND, default operation to combine criteria following one another. Arity 2 (uses the next 2 criteria or combinations).
//	OR("|"),   // logical OR, arity 2.
//	NOT("!") // logical NOT, arity 1.
//}
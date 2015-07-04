package javabot.web.model;

public enum Authority {

  // Naming conventions help navigation and avoid duplication
  // An Authority is named as VERB_SUBJECT_ENTITY
  // Verbs should initially follow CRUD (CREATE, RETRIEVE, UPDATE, DELETE)
  // Subjects are based on outward looking relationships (OWN, OTHERS)
  // Entities are based on primary entities (USER, CUSTOMER, CART, ITEM, INVOICE)

  // Roles (act as EnumSets from the fine grained authorities defined later)

  // Internal roles
  /**
   * The administrator role that can reach administration API functions
   */
  ROLE_ADMIN,
  /**
   * An anonymous (public) customer
   */
  ROLE_PUBLIC,
  /**
   * An un-authenticated customer in possession of a "remember me" token
   */
  ROLE_PARTIAL,

  // End of enum
  ;

}

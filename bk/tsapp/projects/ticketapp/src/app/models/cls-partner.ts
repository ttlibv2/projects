
export interface SearchContact {
    vat?: string;
    mobile?: string;
    email?: string;

    isvat?: true; // vat is set
    ismobile?: true; // mobile is set
    isemail?: true; // email is set
    isperson?: true; // is person
    iscompany?: true; // is company

    //
    operator?: string;
    limit?: number;
    offset?: number;

}
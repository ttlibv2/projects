export interface Schema {
	name: string;
	appsRoot: string;
	pkgsRoot: string;
	ngApp?: boolean;
	skipTest?: boolean;
	skipInstall?: boolean;
}
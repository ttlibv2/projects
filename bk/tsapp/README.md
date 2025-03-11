ng new monorepo --create-application false --minimal true --prefix ts --routing true --skip-tests true --standalone true --ssr false --style scss --view-encapsulation None  --skip-install false

ng g app tsapp --minimal true --prefix ts --server-routing false --skip-tests true --ssr false --style scss --view-encapsulation None

ng g lib ts-ui --prefix ts --skip-tests true

ng g c send-form -p ts --project tskpi --skip-tests true --standalone false --style scss --type view

angular.json
"server": "projects/ticketapp/src/main.server.ts",
"prerender": false,
"ssr": { "entry": "projects/ticketapp/server.ts"  },

tsconfig.app.json
"files": [
"src/main.ts",
"src/main.server.ts",
"server.ts"
],
ng new TsApp --create-application false --minimal true --prefix ts --routing true --skip-tests true --standalone false --ssr true --style scss --view-encapsulation None

ng g app tskpi -p ts --routing true -S true --standalone false --style scss  --view-encapsulation None  --ssr false

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
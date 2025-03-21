# new workspace angular
ng new cli --create-application false -s true --package-manager pnpm -p t  --routing false --server-routing false -g true  --skip-install true -S true --ssr false --style scss  --view-encapsulation None  --directory packages\cli

# new application angular
ng new tskpi --create-application true -s true --package-manager pnpm -p t  --routing true --server-routing false -g true  --skip-install true -S true --ssr false --style scss --view-encapsulation None  --directory apps\tskpi

D:\Angular_CLI\projects\ng-project\packages\cli\dist>pnpm link -g  --loglevel debug
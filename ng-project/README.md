# new workspace angular
ng new lib --create-application false -s true --package-manager pnpm -p t  --routing false --server-routing false -g true  --skip-install true -S true --ssr false --style scss  --view-encapsulation None  --directory packages\lib

# new application angular
ng new tskpi --create-application true -s true --package-manager pnpm -p t  --routing true --server-routing false -g true  --skip-install true -S true --ssr false --style scss --view-encapsulation None  --directory apps\tskpi

ng g lib 
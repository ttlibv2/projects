import { FileDoesNotExistException, Tree } from '@angular-devkit/schematics';

export function deleteAll(host: Tree, path: string, root?: string) {
    if(!!root) path = root + '/' + path;

    if(host.exists(path)) host.delete(path);
    else {

        // try delete if path is folder
        const folder = host.getDir(path);
        if(folder.subfiles.length) folder.subfiles.forEach(f => deleteAll(host, f, path));
        if(folder.subdirs.length) folder.subdirs.forEach(f => deleteAll(host, f, path));

        // try delete folder if empty
        try{host.delete(path);}
        catch(e) {
            if(e instanceof Error && e.constructor.name == 'FileDoesNotExistException') {}
            else throw e;
        }
    }



}
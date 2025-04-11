import { series, task } from 'gulp';
import * as del from 'del';

export const sources = ['bin', 'dist', 'libs'];

task('clean:output', () => {
  const files = sources.map((source) => [
    `${source}/**/*.js`,
    `${source}/**/*.d.ts`,
    `${source}/**/*.js.map`,
    `${source}/**/*.d.ts.map`,
  ]);

  //console.log('cleanOutput: ', files.reduce((a, b) => a.concat(b), []).join('|'));

  const deletedPaths = del.deleteAsync(
    files.reduce((a, b) => a.concat(b), [])
  );

  //console.log(deletedPaths);

  return deletedPaths;




});

task('clean:dirs', done => {
  sources.forEach((source) => deleteEmpty.sync(`${source}/`));
  done();
});

/**
 * Cleans empty dirs
 */
function cleanDirs() {
  sources.forEach((source) => deleteEmpty.sync(`${source}/`));
  //done();
}

task('clean:bundle', series('clean:output'));
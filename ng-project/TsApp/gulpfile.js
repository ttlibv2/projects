'use strict';

const destCss = './projects/ticketapp/public/css/';
const gulp = require('gulp'),
    rename = require('gulp-rename'),
    sass = require('gulp-sass')(require('sass'));

function compileScss(src,dest) {
    return gulp.src([src])
        .pipe(sass().on('error', sass.logError))
        .pipe(rename(file => {file.dirname = '';return destCss;}))
        .pipe(gulp.dest(destCss));
}

//===================
function theme() {
    compileScss('./all-theme/theme/*/*.scss', destCss);
}

function grid() {
    compileScss('./all-theme/primeflex/_index.scss', destCss);
}

gulp.task('theme', theme);
gulp.task('grid', grid);

gulp.task('all', done => {
    gulp.series('grid');
    done();
});
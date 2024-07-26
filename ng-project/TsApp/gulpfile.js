'use strict';

var gulp = require('gulp'),
    // concat = require('gulp-concat'),
    // uglifycss = require('gulp-uglifycss'),
    rename = require('gulp-rename'),
    // flatten = require('gulp-flatten'),
    sass = require('gulp-sass')(require('sass'));

var destCss = './projects/ticketapp/public/css/';


function compileScss() {
    return gulp.src(['./all-theme/theme/*/*.scss'])
        .pipe(sass().on('error', sass.logError))
        .pipe(rename(file => {
            file.dirname = '';

            return './projects/ticketapp/public/css/';
        }))
        .pipe(gulp.dest(destCss));
}



gulp.task('theme', compileScss);
# Team Monitor Project

![Team Monitor Cover](./docs/cover.png "Team Monitor Cover")

[![Netlify Status](https://api.netlify.com/api/v1/badges/dc472724-5dc5-4063-8a67-4a346bdd6be1/deploy-status)](https://app.netlify.com/sites/av-team-monitor/deploys)

## Description
Some years ago created a side project from scratch to keep learning web technologies and improve my personal skills. This is nowhere near perfect project as I didn't use Typescript and some of my components have way to much responsibility.

This project is a team monitor and to track JIRA and QC items to improve team's response times.

As there are times that items just sit there for days and nobody have checked them. The department I used to work had ~130 cross cultural developers, testers, analysts and managers.
That made the sprints quite difficult to monitor and take action in time. 

### [Demo](https://av-team-monitor.netlify.com/)

## Technologies Used
- MySql
- Java Spring MVC
- VueJS2
- Vuetify

## Features
- light/dark theme
- configurable avatars
- dynamic filtering
- sorting columns
- search data
- opening details for each item
- responsive layout

## Setup
- install MySQL 5.7
- create admin user for database
- encrypt user and password using `src/main/java/server/sec/ChangePassword.java`
- put both values in `src/main/java/resources/mysql5.properties`
- import dummy data (`src/main/java/resources/team_monitor_db.sql`) into database (using MySQL Workbench)
- install Java SDK 11.0.6
- download maven dependencies and re-build project
- configure ALM Quality Center settings, JIRA settings in `src/main/java/resources`
- install NPM dependencies in `src/main/java/webapp` (`npm ci` for faster install)
- run Jetty server and go to `http://localhost:8080`

## How to contribute?
Contact me through mail `vilde.andris@gmail.com` with suggestions.

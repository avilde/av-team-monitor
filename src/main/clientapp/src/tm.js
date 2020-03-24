'use strict';
import { bus } from './main';
import { URL } from './globals';
/*
 * --------------------------------------------- --------------------------------------------- --------------------------------------------- *
 *                                                          TEAM MONITOR GLOBAL OBJECT                                                       *
 * --------------------------------------------- --------------------------------------------- --------------------------------------------- *
 */
class TeamMonitor {
	constructor() {
		this.cfg = undefined;
		this.getConfig(); // init
		this.author = 'andris.vilde@tele2.com';
	}

	getConfig() {
		if (!this.cfg) {
			fetch(`${URL}getTeamMonitorData`)
				.then(resp => resp.json())
				.then(data => {
					this.cfg = data;
					this.cfg.settings = this.cfg.settings.reduce(
						(settings, setting) => {
							settings[setting.name] = setting.value;
							return settings;
						}, {}
					);

					this.cfg.settings.currentTeamName = '';

					this.cfg.settings.avatars = {
						enabled: true,
						vertical: false,
						tile: true,
						size: '56px',
						filterNoTasks: false
					};

					this.cfg.settings.table = {
						typeIcon: true,
						priorityIcon: true,
						statusLabel: true,
						teamLogo: true
					};

					this.cfg.settings.darkTheme = this.cfg.settings.darkTheme === 'true';
					this.cfg.settings.qcEnabled = this.cfg.settings.qcEnabled === 'true';
					this.cfg.settings.jiraEnabled = this.cfg.settings.jiraEnabled === 'true';

					this.cfg.teams = this.cfg.teams.reduce((teams, team) => {
						teams[team.teamName] = team;
						team.members = team.members.reduce(
							(members, member) => {
								members[member.login] = member;
								return members;
							}, {});
						return teams;
					}, {});
					bus.$emit('tmLoaded', this);
					this.logConfig();
				})
				.catch(e => {
					bus.$emit(
						'errorMsg',
						`Server is not responding - please inform <span class="err-h">${this.author}<span/>`
					);
				});
		}
	}

	logConfig(logProject) {
		if (this.cfg) {
			console.log(
				`%c ${this.cfg.settings.shortName} %c ${
					this.cfg.settings.name
				} v${this.cfg.settings.version} `,
				'background: #2196F3; color: #fff; font-size: 12px; border-radius: 3px 0 0 3px; font-family: Tahoma;',
				'background: #bee1fd; color: #000; font-size: 12px; border-radius: 0 3px 3px 0; font-family: Tahoma;',
				logProject
				? `| project: ${this.cfg.settings.project}, department: ${this.cfg.settings.department}`
				: ''
			);
		}
	}
}

export default new TeamMonitor();

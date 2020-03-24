<template>
<div id="tm-app">
	<v-app :dark="settings.darkTheme" :light="!settings.darkTheme">	
		<app-header 
			:teams="teams" 
			:settings="settings" 
			:current-team="currentTeam" 
			:filter="filter"
			:window-size="windowSize"
		/>
		
		<template v-if="teams && !settings.avatars.vertical && settings.avatars.enabled">
			<team-avatar-panel 
				:teams="teams" 
				:current-team="currentTeam"
				:current-member="currentMember"
				:settings="settings"
			/>
		</template>

		<task-table 
			:teams="teams" 
			:settings="settings" 
			:current-team="currentTeam"
			:current-member="currentMember"
			:window-size="windowSize"
			:filter="filter"
		>
			<v-flex 
				class="tm-avatar-panel-container ml-1" 
				slot="avatars" 
				v-if="teams && settings && settings.avatars.vertical && settings.avatars.enabled"
			>
				<team-avatar-panel 
					:teams="teams"
					:current-member="currentMember"
					:current-team="currentTeam"
					:settings="settings"
					vertical
				></team-avatar-panel>
			</v-flex>
		</task-table>

		<app-footer :settings="settings"/>
	</v-app>
</div>
</template>

<script>
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';
import TaskTable from './components/TaskTable.vue';
import TeamAvatarPanel from './components/TeamAvatarPanel.vue';
import {capitalize} from './globals';

import {bus} from './main';

const FILTER_CFG = Object.freeze({
	tasks: [],
	labels: [],
	closedSprints: [],
	activeSprints: [],
	futureSprints: [],
	types: [],
	statuses: [],
	priorities: [],
	lastModified: [],
	noSprint: false,
	closed: false,
	active: false,
	future: false
});

export default {
	name: 'tm-app',

	data() {
		return {
			teams: undefined,

			settings: undefined,

			currentTeam: undefined,

			currentMember: undefined,

			defaultPrimaryColor: '',

			defaultSecondaryColor: '',

			filter: JSON.parse(JSON.stringify(FILTER_CFG))
		};
	},

	components: {
		appHeader: Header,
		appFooter: Footer,
		taskTable: TaskTable,
		teamAvatarPanel: TeamAvatarPanel
	},

	created() {
		bus.$on('teamSelected', team => {
			this.currentTeam = team;
			this.currentMember = undefined;

			let setup = {...this.settings};
			setup.primaryColor = team ? team.primaryColor : this.defaultPrimaryColor;
			setup.secondaryColor = team ? team.secondaryColor : this.defaultSecondaryColor;
			this.settings = setup;
			this.settings.currentTeamName = team ? team.teamName : '';
			this.setOptionsCookie();

			document.title = `${capitalize(this.currentTeam ? this.currentTeam.teamName : 'Team')} Monitor­`;
		});

		bus.$on('taskDataRdy', data => {
			this.teams = this.updateTaskCount(data, this.teams);
			this.currentTeam = this.currentTeam ? {...this.teams[this.currentTeam.teamName]} : undefined;
		});

		bus.$on('memberSelected', member => {
			this.currentMember = member;
		});

		bus.$on('settingAvatarsChanged', avatar => {
			let setup = {...this.settings};
			setup.avatars = avatar;

			this.settings = setup;
			this.setOptionsCookie();
		});

		bus.$on('settingTableChanged', table => {
			let setup = {...this.settings};
			setup.table = table;

			this.settings = setup;
			this.setOptionsCookie();
		});

		bus.$on('settingDarkThemeChanged', state => {
			let setup = {...this.settings};
			setup.darkTheme = state;

			this.settings = setup;
			this.setOptionsCookie();
		});

		bus.$on('toggleAvatars', _ => {
			this.settings.avatars.enabled = !this.settings.avatars.enabled;
			bus.$emit('settingAvatarsChanged', this.settings.avatars);
		});

		// rest of magic
		this.teams = {...window.TM.cfg.teams};
		this.settings = {...window.TM.cfg.settings};
		this.defaultPrimaryColor = window.TM.cfg.settings.primaryColor;
		this.defaultSecondaryColor = window.TM.cfg.settings.secondaryColor;

		// options
		const options = this.getOptionsCookie();
		if (options) {
			this.settings.darkTheme = options.darkTheme;
			this.settings.avatars = options.avatars;
			this.settings.table = options.table;
			this.settings.currentTeamName = options.currentTeamName;
		}

		if (this.settings.currentTeamName) {
			this.currentTeam = this.teams[this.settings.currentTeamName];
			this.settings.primaryColor = this.currentTeam.primaryColor;
			this.settings.secondaryColor = this.currentTeam.secondaryColor;
		}

		document.title = (this.currentTeam ? capitalize(this.currentTeam.teamName) : 'Team') + ' Monitor';

		this.windowSize = {w: window.innerWidth, h: window.innerHeight};

		// filter
		bus.$on('filter-selected', filter => {
			let labelIdx = this.filter[filter.type].indexOf(filter.label);

			if (labelIdx > -1) this.filter[filter.type].splice(labelIdx, 1);
			else this.filter[filter.type].push(filter.label);
		});

		bus.$on('clear-filters', this.clearFilter);

		bus.$on('filter-sprint', payload => {
			this.filter[payload.key] = payload.val;
		});
	},

	methods: {
		updateTaskCount(tableData, teamData) {
			let teams = {...teamData},
				tasks = [...tableData];

			for (const team in teams) {
				teams[team].taskCount = 0;
				for (const member in teams[team].members) teams[team].members[member].taskCount = 0;
			}

			if (teams && tableData) {
				tasks.forEach(item => {
					if (item.team && teams.hasOwnProperty(item.team)) {
						++teams[item.team].taskCount;
						if (item.assignee && teams[item.team].members.hasOwnProperty(item.assignee)) ++teams[item.team].members[item.assignee].taskCount;
					}
				});
			}

			return teams;
		},

		getOptionsCookie() {
			const cookie = this.$cookie.get('options');
			return cookie ? JSON.parse(cookie) : undefined;
		},

		setOptionsCookie() {
			this.$cookie.set('options', JSON.stringify(this.settings), Infinity);
		},

		clearFilter() {
			this.filter = JSON.parse(JSON.stringify(FILTER_CFG));
		}
	}
};
</script>

<style lang="stylus">
@import '~vuetify/src/stylus/main.styl';
</style>

<style>
html {
	overflow-y: auto !important;
}

.tm-scrollbar::-webkit-scrollbar {
	width: 7px;
	height: 7px;
}

.tm-scrollbar.dark::-webkit-scrollbar {
	background-color: #424242;
}

.tm-scrollbar.light::-webkit-scrollbar {
	background-color: #f5f5f5;
}

.tm-scrollbar::-webkit-scrollbar-track {
	-webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
	box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
	background-color: #f5f5f5;
	border-radius: 5px;
}

.tm-scrollbar::-webkit-scrollbar-thumb {
	background-color: #c4c4c4;
	border-radius: 5px;
}

.tm-scrollbar::-webkit-scrollbar-thumb:active {
	background-color: #6b6b6b;
}

.no-select {
	-webkit-touch-callout: none;
	-webkit-user-select: none;
	-khtml-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

.no-overflow {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

/* statuses */
span.status {
	border-radius: 3px;
	border: 1px solid #ccc;
	padding: 2px 5px;
	font-size: 11px;
	font-weight: bold;
	text-transform: uppercase;
	text-align: center;
	text-decoration: none;
	display: inline-block;
}

span.status[status='user acceptance'],
span.status[status='in sprint'],
span.status[status='in progress'],
span.status[status='in testing'],
span.status[status='in progress'] {
	background-color: #fdb838;
	border-color: #fdb838;
	color: #594300;
}
span.status[status='refined'],
span.status[status='new'],
span.status[status='sprint ready'],
span.status[status='in refinement'],
span.status[status='groomed'],
span.status[status='draft'],
span.status[status='to do'] {
	background-color: #2969b3;
	border-color: #2969b3;
	color: #fff;
}

/* priority */
i.priority-icon {
	font-size: 22px;
}

i.priority-icon[priority='unprioritized'] {
	color: orange;
}

i.priority-icon[priority='blocked'],
i.priority-icon[priority='critical'],
i.priority-icon[priority='major'],
i.priority-icon[priority='medium'] {
	color: #fa5f5f;
}

i.priority-icon[priority='medium'] {
	transform: rotate(-90deg);
}

i.priority-icon[priority='trivial'] {
	color: gray;
	transform: rotate(90deg);
}

i.priority-icon[priority='minor'] {
	color: #38ad38;
	transform: rotate(90deg);
}

span.tm-divider-v {
    border-left: 1px solid #808080;
    margin-left: 20px;
    padding-right: 20px;
    margin-top: 5px;
    margin-bottom: 5px;
}
</style>


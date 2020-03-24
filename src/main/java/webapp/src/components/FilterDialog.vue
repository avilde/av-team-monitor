<template>
  <v-dialog
	v-model="open"
	content-class="tm-filter-dialog tm-scrollbar"
	@keydown.esc="open = false"
	origin="top center"
>
	<v-card>
		<v-card-title 
			:class="[
				!settings.darkTheme ? 'white--text' : 'black--text',
				settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
				!settings.darkTheme ? 'accent-5' : 'lighten-3',
				'tm-filter-header'
			]"
		>
			<v-flex align-content-start>
				<span>Choose filters</span>
				
				<v-tooltip right>
					<fa-icon 
						class="tm-filter-info" 
						icon="info-circle" 
						size="lg"
						slot="activator"
						:color="settings.darkTheme ? 'grey' : 'white'"
					/>
					<span>Filter labels are generated based on data retrieved from JIRA items and current selected team. Some filters may not visible in other teams.</span>
				</v-tooltip>
			</v-flex>
		</v-card-title>

		<v-card-text>
			<v-container fluid wrap class="tm-filter-container tm-scrollbar">

				<v-flex class="mb-4 elevation-1 tm-dialog-actions">
					<v-btn
						flat
						color="gray"
						@click="clearAll()"
						small
						title="Clear All Filters"
						:disabled="noFilterSelected"
					>Clear All<fa-icon medium :color="[noFilterSelected ? 'grey': '#ff1744']" icon="times-circle" class="ml-2" size="lg"></fa-icon></v-btn>
					
					<span class="tm-divider-v"></span>

					<v-switch 
						v-model="noSprint"
						class="tm-filter-switch"
						:color="[
							settings.primaryColor,
							'ligthen-1'
						].join(' ')" 
					>
						<div slot="label" class="body-1 tm-filter-switch">no sprint</div>
					</v-switch>

					<v-switch 
						v-model="closed" 
						class="tm-filter-switch"
						:color="[
							settings.primaryColor,
							'ligthen-1'
						].join(' ')" 
					>
						<div slot="label" class="body-1 tm-filter-switch">closed sprints</div>
					</v-switch>

					<v-switch 
						v-model="active"
						class="tm-filter-switch"
						:color="[
							settings.primaryColor,
							'ligthen-1'
						].join(' ')" 
					>
						<div slot="label" class="body-1 tm-filter-switch">active sprints</div>
					</v-switch>

					<v-switch 
						v-model="future"
						class="tm-filter-switch"
						:color="[
							settings.primaryColor,
							'ligthen-1'
						].join(' ')" 
					>
						<div slot="label" class="body-1 tm-filter-switch">future sprints</div>
					</v-switch>
				</v-flex>

				<v-flex class="mb-2">
				
					by <b>Issue Type</b>:

					<filter-chip
						:key="l" 
						v-for="l in ['parent', 'child']"
						:settings="settings"
						:label="l"
						type="tasks"
						:filter="filter"
					/>						
				</v-flex>

				<v-flex class="mb-2" v-if="sprintsExist">
					<v-divider class="mb-2"></v-divider>

					by <b>Sprint</b>:
					<div v-if="closed && filters.closedSprints && filters.closedSprints.length > 0" class="mb-1 ml-4">
						<strike>closed</strike>:
						<filter-chip
							:key="l" 
							v-for="l in filters.closedSprints"
							:settings="settings"
							:label="l"
							type="closedSprints"
							:filter="filter"
						/>
					</div>

					<div v-if="active && filters.activeSprints && filters.activeSprints.length > 0" class="mb-1 ml-4">
						<b>active</b>:
						<filter-chip
							:key="l" 
							v-for="l in filters.activeSprints"
							:settings="settings"
							:label="l"
							type="activeSprints"
							:filter="filter"
						/>
					</div>

					<div v-if="future && filters.futureSprints && filters.futureSprints.length > 0" class="ml-4">
						<i>future</i>:
						<filter-chip
							:key="l" 
							v-for="l in filters.futureSprints"
							:settings="settings"
							:label="l"
							type="futureSprints"
							:filter="filter"
						/>
					</div>

				</v-flex>

				<v-flex class="mb-2" v-if="filters.types && filters.types.length > 0">
					<v-divider class="mb-2"></v-divider>

					by <b>Type</b>:

					<filter-chip
						:key="l" 
						v-for="l in filters.types"
						:settings="settings"
						:label="l"
						type="types"
						:filter="filter"
					/>
				</v-flex>

				<v-flex class="mb-2" v-if="filters.labels && filters.labels.length > 0">
					<v-divider class="mb-2"></v-divider>
				
					by <b>Label</b>:

					<filter-chip
						:key="l" 
						v-for="l in filters.labels"
						:settings="settings"
						:label="l"
						type="labels"
						:filter="filter"
					/>						
				</v-flex>

				<v-flex class="mb-2" v-if="filters.statuses && filters.statuses.length > 0">
					<v-divider class="mb-2"></v-divider>

					by <b>Status</b>:

					<filter-chip
						:key="l" 
						v-for="l in filters.statuses"
						:settings="settings"
						:label="l"
						type="statuses"
						:filter="filter"
					/>
				</v-flex>

				<v-flex class="mb-2" v-if="filters.priorities && filters.priorities.length > 0">
					<v-divider class="mb-2"></v-divider>

					by <b>Priority</b>:

					<filter-chip
						:key="l" 
						v-for="l in filters.priorities"
						:settings="settings"
						:label="l"
						type="priorities"
						:filter="filter"
					/>
				</v-flex>

				<v-flex class="mb-2">
					<v-divider class="mb-2"></v-divider>

					by <b>Last Modified</b>:

					<filter-chip
						:key="l" 
						v-for="l in ['today', 'yesterday', 'this week', 'last week', 'this month']"
						:settings="settings"
						:label="l"
						type="lastModified"
						:filter="filter"
					/>
				</v-flex>
			</v-container>
		</v-card-text>

		<v-card-actions class="tm-filter-actions">
			<v-btn color="primary" flat @click.stop="open = false">Close</v-btn>
		</v-card-actions>
	</v-card>
	</v-dialog>
</template>

<script>
import {bus} from '../main';
import FilterChip from '../components/FilterChip.vue';

export default {
	data() {
		return {
			open: false,

			itemData: [],

			noSprint: false,
			closed: false,
			active: false,
			future: false
		};
	},

	props: {
		settings: {
			type: Object,
			required: true
		},

		filter: {
			type: Object,
			required: true
		},

		currentTeam: {
			type: Object
		}
	},

	components: {
		FilterChip
	},

	computed: {
		noFilterSelected() {
			return (
				!this.filter.noSprint &&
				!this.filter.closed &&
				!this.filter.active &&
				!this.filter.future &&
				this.filter.tasks.length === 0 &&
				this.filter.labels.length === 0 &&
				this.filter.closedSprints.length === 0 &&
				this.filter.activeSprints.length === 0 &&
				this.filter.futureSprints.length === 0 &&
				this.filter.types.length === 0 &&
				this.filter.statuses.length === 0 &&
				this.filter.priorities.length === 0 &&
				this.filter.lastModified.length === 0
			);
		},

		filters() {
			let newFilter = {
					tasks: [],
					labels: [],
					closedSprints: [],
					activeSprints: [],
					futureSprints: [],
					types: [],
					statuses: [],
					priorities: [],
					lastModified: []
				},
				data = this.itemData;

			if (this.currentTeam) data = data.filter(item => item.team === this.currentTeam.teamName);

			data.forEach(item => {
				let sprint, sprintName, type, status, prio;

				if (item.labels) {
					JSON.parse(item.labels).map(label => {
						if (label && newFilter.labels.indexOf(label.toLowerCase()) === -1) newFilter.labels.push(label.toLowerCase());
					});
				}

				if (item.sprints) {
					sprint = JSON.parse(item.sprints).find(s => s.state !== '');
					if (sprint) {
						sprintName = sprint.name.toLowerCase();
						if (newFilter.closedSprints.indexOf(sprintName) === -1 && sprint.state === 'CLOSED') newFilter.closedSprints.push(sprintName);
						if (newFilter.activeSprints.indexOf(sprintName) === -1 && sprint.state === 'ACTIVE') newFilter.activeSprints.push(sprintName);
						if (newFilter.futureSprints.indexOf(sprintName) === -1 && sprint.state === 'FUTURE') newFilter.futureSprints.push(sprintName);
					}
				}

				type = item.type.toLowerCase();
				if (type && newFilter.types.indexOf(type) === -1) newFilter.types.push(type);

				status = item.status.toLowerCase();
				if (status && newFilter.statuses.indexOf(status) === -1) newFilter.statuses.push(status);

				prio = item.priority.toLowerCase();
				if (prio && newFilter.priorities.indexOf(prio) === -1) newFilter.priorities.push(prio);
			});

			return newFilter;
		},

		sprintsExist() {
			return (
				!this.noSprint &&
				this.filters &&
				((this.closed && this.filters.closedSprints.length > 0) || (this.active && this.filters.activeSprints.length > 0) || (this.future && this.filters.futureSprints.length > 0))
			);
		}
	},

	created() {
		bus.$on('openFilter', _ => {
			this.open = true;
		});

		bus.$on('taskDataRdy', data => {
			data.map(item => {
				return Object.freeze(item);
			});

			this.itemData = data;
		});
	},

	methods: {
		clearAll() {
			bus.$emit('clear-filters');
			this.noSprint = false;
			this.closed = false;
			this.active = false;
			this.future = false;
		}
	},

	watch: {
		noSprint(newVal) {
			bus.$emit('filter-sprint', {key: 'noSprint', val: newVal});
			if (newVal) {
				this.closed = false;
				this.active = false;
				this.future = false;
			}
		},

		closed(newVal) {
			bus.$emit('filter-sprint', {key: 'closed', val: newVal});
			if (newVal) {
				this.noSprint = false;
				this.active = false;
				this.future = false;
			}
		},

		active(newVal) {
			bus.$emit('filter-sprint', {key: 'active', val: newVal});
			if (newVal) {
				this.noSprint = false;
				this.closed = false;
				this.future = false;
			}
		},

		future(newVal) {
			bus.$emit('filter-sprint', {key: 'future', val: newVal});
			if (newVal) {
				this.noSprint = false;
				this.closed = false;
				this.active = false;
			}
		}
	}
};
</script>

<style>
.tm-filter-dialog {
	position: absolute;
	top: 30px;
	left: 20%;
	border-radius: 5px;
	max-width: 60%;
}

.tm-dialog-actions {
	border-radius: 3px;
	justify-content: center;
	display: flex;
}

.tm-dialog-actions .input-group__details {
	display: none;
}

#tm-app .tm-filter-container .tm-dialog-actions label {
	line-height: 40px !important;
}

.tm-filter-header {
	display: flex;
	height: 40px;
	justify-content: space-between;
}

.tm-filter-info {
	cursor: help;
	margin: 0 10px;
}

.tm-filter-info:hover {
	color: #397ecc;
}

@media screen and (max-width: 1399px) {
	.tm-filter-dialog {
		position: absolute;
		top: 0;
		left: 0;
		border-radius: 5px;
		max-width: 95%;
	}
}

@media screen and (max-width: 849px) {
	.tm-dialog-actions {
		height: auto !important;
		padding-left: 20px !important;
		flex-wrap: wrap;
		padding: 5px;
	}

	.tm-filter-switch {
		min-width: 140px !important;
		min-height: 30px !important;
	}
}

@media screen and (max-width: 767px) {
	.tm-filter-dialog {
		position: absolute;
		top: 0;
		left: 0;
		border-radius: 5px;
		max-width: 90%;
	}
}

@media screen and (max-width: 480px) {
	.tm-dialog-actions {
		height: 200px !important;
		flex-direction: column;
		padding-left: 20px !important;
	}
}
</style>

<template>
	<v-container fluid class="tm-task-table-card"> 
		<v-layout class="tm-task-table-layout" v-resize="calcWindowSize">
				<v-flex xs12 offset-xs0 class="tm-task-table-flex">
					<v-data-table 
						:items="filteredItems" 
						:headers="headers"
						:search="search"
						:rows-per-page-items="calcRowCount()"
						must-sort
						item-key="rowId"
						:pagination.sync="pagination"
						:loading="loading"
						:custom-filter="customFilter"
						:filter="itemFilter"
					>
						<template slot="headers" slot-scope="props">
							<tr>
								<th v-for="(header, index) in props.headers" :key="header.text"
									:class="[
										'column',
										'no-select',
										'tm-table-th',
										'idx-' + index,
										typeof header.sortable === 'undefined' || header.sortable === true ? 'sortable' : '',
										pagination.descending ? 'desc' : 'asc',
										(typeof header.sortable === 'undefined' || header.sortable === true) && header.value === pagination.sortBy ? 'active' : '',
										header.align ? 'text-xs-' + header.align : 'text-xs-right',
										'title',
										!settings.darkTheme ? 'white--text' : 'black--text',
										settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
										!settings.darkTheme ? 'accent-5' : 'lighten-3',
										settings.avatars.vertical ? 'vertical' : ''
									]"
									@click="(typeof header.sortable === 'undefined' || header.sortable === true) ? changeSort(header.value) : null"
									v-show="(!currentTeam || index !== 3) && (!currentMember || index !== 2)"
									:style="index === 1 ? {'max-width': summaryWidth} : ''"
								>
									<v-icon 
										v-if="typeof header.sortable === 'undefined' || header.sortable === true"
										:class="!settings.darkTheme ? 'white--text' : 'black--text'"
									>arrow_upward</v-icon>
									{{ header.text }}
								</th>
							</tr>
						</template>
						<template slot="items" slot-scope="props">
							<tr @dblclick="openTaskDetails(props.item)" class="no-select">
								<td 
									:class="[
										'subheading tm-table-td idx-0',
										settings.darkTheme ? 'dark' : '',
										'no-overflow'
									]"
								>
									<span 
										class="type-icon" 
										:title="props.item.type.toLowerCase()"
										v-if="settings.table.typeIcon"
									>
										<fa-icon 
											:style="{color: getTypeIcon(props.item.type).color}"
											:icon="getTypeIcon(props.item.type).icon"
										></fa-icon>
									</span>

									<a
										v-if="isJiraItem(props.item)"
										target="_blank"
										rel="noopener"
										:href="jiraItemUrl(props.item)"
									> {{ props.item.itemKey }}</a>
									<template v-else>{{ props.item.itemKey }}</template>
								</td>
								
								<td 
									class="text-lg-left body-2 tm-table-td idx-1 no-overflow"
									:class="[settings.avatars.vertical ? 'vertical' : '']"
									:style="{'max-width': summaryWidth}"
								>
									<span 
										:class="{
											'parent': true,
											'dark': settings.darkTheme
										}"
										v-if="props.item.isSubtask && parent(props.item)"
									>
										<a
											target="_blank"
											rel="noopener"
											:href="jiraItemUrl(parent(props.item))"
											:class="{
												'dark': settings.darkTheme
											}"
										> {{ parent(props.item).itemKey }}</a>
										{{ parent(props.item).summary }} / 
									</span>
									<span>{{ props.item.summary }}</span>
								</td>
								
								<td 
									class="text-xs-right body-2 tm-table-td idx-2"
									v-if="!currentMember"
								>{{ props.item.assignee }}</td>
								
								<td 
									class="text-xs-center body-2 tm-table-td idx-3"
									v-if="!currentTeam"
								>
									<v-avatar
										v-if="settings.table.teamLogo"
										size="32px"
										tile
										class="tm-table-team-avatar tm-avatar-tile"
										:title="props.item.team"
									>
										<picture>
											<source :srcset="`../static/images/logos/${props.item.team}-32.webp`" type="image/webp">
											<img :src="`../static/images/logos/${props.item.team}-32.png`" :alt="props.item.team">
										</picture>
									</v-avatar>
									<template v-else>
										{{ props.item.team }}
									</template>
								</td>
								
								<td class="text-xs-center body-2 tm-table-td idx-4 no-overflow">
									<span
										v-if="settings.table.statusLabel" 
										class="status"
										:status="props.item.status.toLowerCase()"
									>{{ props.item.status }}</span>
									<template v-else>{{ props.item.status }}</template>
								</td>
								
								<td class="text-xs-right body-2 tm-table-td idx-5">
									<v-icon 
										class="priority-icon" 
										:priority="props.item.priority.toLowerCase()"
										v-if="settings.table.priorityIcon"
										:title="props.item.priority.toLowerCase()"
									>{{ priority(props.item.priority.toLowerCase()) }}</v-icon>
									<span 
										class="tm-table-priority-text"
										v-else
									>{{ props.item.priority }}</span>
								</td>
								
								<td class="text-xs-right body-2 tm-table-td idx-6">{{ moment(props.item.updated).fromNow() }}</td>
								
								<td class="text-xs-right tm-info-column tm-table-td idx-7 no-select">
									<v-icon 
										class="tm-info-icon" 
										:color="[
											settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
											settings.darkTheme ? 'lighten-4' : 'lighten-1'
										].join(' ')" 
										medium 
										@click="openTaskDetails(props.item)"
										title="Click to see details"
									>info</v-icon>
								</td>
							</tr>
						</template>
					</v-data-table>
				</v-flex>

				<v-flex>
					<slot name="avatars"></slot>
				</v-flex>
		</v-layout>

		<task-details
			:settings="settings"
			:current-item="currentItem"
		></task-details>
	</v-container>
	
</template>

<script>
import moment from 'moment';
import {bus} from '../main';
import TaskDetails from './TaskDetails.vue';
import {debounce, getJiraItems, getQcItems, PRIO_ICONS, TYPE_ICONS} from '../globals';
import jiraItemsMock from '../mock/jira-items.json';

const QC = 'QC',
	JIRA = 'JIRA',
	CHILD = 'child',
	PARENT = 'parent';

export default {
	components: {
		TaskDetails
	},

	data() {
		return {
			items: [],
			// TODO: add keys 'class' and 'width' for headers
			headers: [
				{text: '#', align: 'left', value: 'itemKey'},
				{text: 'Summary', align: 'left', value: 'summary'},
				{text: 'Assignee', value: 'assignee'},
				{text: 'Team', value: 'team'},
				{text: 'Status', value: 'status'},
				{text: 'Priority', value: 'priority'},
				{text: 'Updated', value: 'updated'},
				{text: 'Info', sortable: false}
			],

			pagination: {
				page: 1,
				descending: 'desc',
				sortBy: 'status'
			},

			search: '',

			loading: true,

			moreInfoDialog: false,

			currentItem: undefined,

			windowWidth: this.windowSize.w,

			windowHeight: this.windowSize.h,

			summaryWidth: 0,

			rowCount: undefined
		};
	},

	props: {
		teams: {
			type: Object
		},

		settings: {
			type: Object
		},

		currentTeam: {
			type: Object
		},

		currentMember: {
			type: Object,
			default: undefined
		},

		windowSize: {
			type: Object
		},

		filter: {
			type: Object,
			required: true
		}
	},

	computed: {
		filteredItems() {
			let vm = this,
				resultItems = vm.items,
				f = vm.filter,
				filterFunc;
			// issue types
			if (f && f.tasks && f.tasks.length > 0) {
				if (f.tasks.length === 1 && f.tasks[0] === CHILD) filterFunc = item => item.isSubtask === true;
				else if (f.tasks.length === 1 && f.tasks[0] === PARENT) filterFunc = item => item.isSubtask === false;
				else filterFunc = item => true;

				resultItems = resultItems.filter(filterFunc);
			}
			// labels
			if (f && f.labels && f.labels.length > 0) {
				resultItems = f.labels.reduce((r, l) => {
					return r.concat(resultItems.filter(item => item.labels.toLowerCase().indexOf(l) > -1));
				}, []);
			}
			// types
			if (f && f.types && f.types.length > 0) resultItems = resultItems.filter(item => f.types.indexOf(item.type.toLowerCase()) > -1);
			// sprints
			if (f) {
				resultItems = resultItems.filter(
					item =>
						(!f.noSprint && !f.closed && !f.active && !f.future) ||
						(f.noSprint && item.sprints === '[]') ||
						(f.closed && item.sprints.indexOf('CLOSED') > -1) ||
						(f.active && item.sprints.indexOf('ACTIVE') > -1) ||
						(f.future && item.sprints.indexOf('FUTURE') > -1)
				);
			}
			if (f && f.closedSprints && f.closedSprints.length > 0) {
				resultItems = f.closedSprints.reduce((r, s) => {
					return r.concat(resultItems.filter(item => item.sprints.toLowerCase().indexOf(s) > -1));
				}, []);
			}
			if (f && f.activeSprints && f.activeSprints.length > 0) {
				resultItems = f.activeSprints.reduce((r, s) => {
					return r.concat(resultItems.filter(item => item.sprints.toLowerCase().indexOf(s) > -1));
				}, []);
			}
			if (f && f.futureSprints && f.futureSprints.length > 0) {
				resultItems = f.futureSprints.reduce((r, s) => {
					return r.concat(resultItems.filter(item => item.sprints.toLowerCase().indexOf(s) > -1));
				}, []);
			}
			// statuses
			if (f && f.statuses && f.statuses.length > 0) resultItems = resultItems.filter(item => f.statuses.indexOf(item.status.toLowerCase()) > -1);
			// priorities
			if (f && f.priorities && f.priorities.length > 0) resultItems = resultItems.filter(item => f.priorities.indexOf(item.priority.toLowerCase()) > -1);
			// last modified
			if (f && f.lastModified && f.lastModified.length > 0) {
				resultItems = f.lastModified.reduce((r, lm) => {
					switch (lm) {
						case 'today':
							return r.concat(resultItems.filter(item => vm.moment(item.updated).isSame(vm.moment(), 'day')));
						case 'yesterday':
							return r.concat(resultItems.filter(item => vm.moment(item.updated).isSame(vm.moment().subtract(1, 'day'), 'day')));
						case 'this week':
							return r.concat(resultItems.filter(item => vm.moment(item.updated).isSame(vm.moment(), 'week')));
						case 'last week':
							return r.concat(resultItems.filter(item => vm.moment(item.updated).isSame(vm.moment().subtract('1', 'week'), 'week')));
						case 'this month':
							return r.concat(resultItems.filter(item => vm.moment(item.updated).isSame(vm.moment(), 'month')));
					}
				}, []);
			}

			if (vm.currentTeam) resultItems = resultItems.filter(item => item.team === vm.currentTeam.teamName);

			if (vm.currentMember) resultItems = resultItems.filter(item => item.assignee === vm.currentMember.login);

			return Array.from(new Set(resultItems));
		},

		isJiraItem() {
			return item => item.internalType === JIRA;
		},

		jiraItemUrl() {
			return item => (item ? `${this.settings.jiraHost}/browse/${item.itemKey}` : undefined);
		},

		priority() {
			return priority => {
				return PRIO_ICONS[priority];
			};
		},

		parent() {
			return item => {
				return item.parent ? JSON.parse(item.parent) : undefined;
			};
		}
	},

	async created() {
		this.calcWindowSize();

		this.getData(true);

		bus.$on('taskDataRdy', data => {
			data = data.map(item => {
				return Object.freeze(item);
			});
			this.items = data;
		});

		bus.$on('taskSearch', searchString => {
			this.search = searchString;
		});

		bus.$on('refreshTasks', this.getData);

		bus.$on('closeTaskDetails', _ => {
			this.moreInfoDialog = false;
		});

		bus.$on('teamSelected', this.resetPagination);

		bus.$on('memberSelected', this.resetPagination);
	},

	mounted() {
		this.calcSummaryWidth();
	},

	methods: {
		async getData(initial) {
			let vm, qcData, jiraData, result;

			vm = this;

			vm.loading = true;
			bus.$emit('tasksLoading', initial);

			qcData = vm.settings && vm.settings.qcEnabled ? await getQcItems() : [];
			
			try {
			jiraData = vm.settings && vm.settings.jiraEnabled ? await getJiraItems() : [];
			} catch (e) {
				jiraData = jiraItemsMock;
			}
			result = [];

			if (vm.settings.qcEnabled) {
				result = result.concat(
					qcData.map(rec => {
						rec.internalType = QC;
						return rec;
					})
				);
			}

			if (vm.settings.jiraEnabled) {
				result = result.concat(
					jiraData.map(rec => {
						rec.internalType = JIRA;
						return rec;
					})
				);
			}

			vm.loading = false;
			bus.$emit('tasksLoaded', initial);
			bus.$emit('taskDataRdy', result);
		},

		openTaskDetails(item) {
			this.currentItem = item;
			bus.$emit('openTaskDetails');
		},

		changeSort(column) {
			if (this.pagination.sortBy === column) this.pagination.descending = !this.pagination.descending;
			else {
				this.pagination.sortBy = column;
				this.pagination.descending = false;
			}
		},

		moment(...args) {
			return moment(...args);
		},

		resetPagination() {
			this.pagination.page = 1;
		},

		getTypeIcon(type) {
			return TYPE_ICONS.hasOwnProperty(type.toLowerCase()) ? TYPE_ICONS[type.toLowerCase()] : TYPE_ICONS['default'];
		},

		calcSummaryWidth() {
			const newWidth = (this.windowWidth - 140) * (this.settings.vertical ? 0.35 : 0.4);
			this.summaryWidth = (newWidth > 140 ? newWidth : 140) + 'px';
		},

		calcRowCount() {
			// header - footer - table header - table footer - ?avatar panel / row height
			if (!this.rowCount) {
				let availableHeight = this.windowHeight - 64 - 30 - 60 - 60 - (!this.settings.avatars.vertical ? 120 : 0);
				this.rowCount = [Math.round(availableHeight / 48)];
			}
			return this.rowCount;
		},

		calcWindowSize: debounce(function() {
			this.windowWidth = window.innerWidth;
			this.windowHeight = window.innerHeight;
		}, 50),

		itemFilter(val, search) {
			return (
				val != null &&
				typeof val !== 'boolean' &&
				val
					.toString()
					.toLowerCase()
					.indexOf(search) !== -1
			);
		},

		customFilter(items, search, filter) {
			search = search.toString().toLowerCase();
			if (search.trim() === '') return items;

			return items.filter(item => Object.keys(item).some(key => filter(item[key], search)));
		}
	},

	watch: {
		windowWidth(nv, ov) {
			this.calcSummaryWidth();
		},

		windowHeight(nv, ov) {
			this.calcRowCount();
		}
	}
};
</script>

<style>
#tm-app .tm-task-table-card {
	padding: 0 0 30px 0;
}

#tm-app th.active {
	opacity: 0.85;
}

#tm-app i.tm-info-icon:hover {
	cursor: pointer;
	opacity: 0.9;
}

#tm-app i.tm-info-icon:active {
	cursor: pointer;
	opacity: 0.5;
}

#tm-app .idx-0 {
	min-width: 130px;
	max-width: 130px;
}

#tm-app .idx-2,
#tm-app .idx-3,
#tm-app .idx-5,
#tm-app .idx-6 {
	min-width: 110px;
	max-width: 110px;
}

#tm-app .idx-4 {
	min-width: 120px;
	max-width: 120px;
}

#tm-app .idx-7 {
	max-width: 70px;
	min-width: 70px;
}

#tm-app .tm-table-td {
	max-height: 50px;
}

#tm-app .idx-0.dark {
	color: lightgrey;
}

#tm-app .idx-0.dark a {
	color: white;
}

#tm-app .idx-0 a:hover {
	color: #ff9800;
}

#tm-app .parent,
#tm-app .parent a {
	color: grey;
}

#tm-app .parent:hover {
	color: black;
}

#tm-app .parent.dark:hover {
	color: white;
}

#tm-app .parent:hover > a {
	color: #1976d2;
}

#tm-app .parent.dark:hover > a.dark {
	color: #5aaafa;
}

#tm-app .parent a:hover,
#tm-app .parent.dark a.dark:hover {
	color: #ff9800;
}

#tm-app .parent:hover > a.dark {
	color: white;
}

@media screen and (max-width: 1340px) {
	#tm-app .idx-6 {
		display: none;
	}
}

@media screen and (max-width: 1100px) {
	#tm-app .idx-5 {
		display: none;
	}
}

@media screen and (max-width: 830px) {
	#tm-app .idx-4 {
		display: none;
	}
}

@media screen and (max-width: 768px) {
	#tm-app .idx-3,
	#tm-app .idx-4 {
		display: none;
	}
}

@media screen and (max-width: 620px) {
	#tm-app .idx-2 {
		display: none;
	}
}

#tm-app th.tm-table-th,
#tm-app td.tm-table-td {
	padding: 0 10px;
}

#tm-app .idx-0 .type-icon {
	min-width: 21px;
	display: inline-block;
	text-align: center;
}

@media screen and (max-width: 620px) {
	#tm-app .datatable__actions {
		justify-content: center;
	}

	#tm-app div.datatable__actions__select {
		font-size: 0;
	}

	#tm-app th.tm-table-th,
	#tm-app td.tm-table-td {
		padding: 0 10px;
	}

	#tm-app .idx-0 {
		min-width: 110px;
		max-width: 110px;
	}

	#tm-app .parent {
		display: none;
	}
}

@media screen and (max-width: 480px) {
	#tm-app td.tm-table-td,
	#tm-app td.tm-table-th {
		padding: 5px;
	}
}
</style>
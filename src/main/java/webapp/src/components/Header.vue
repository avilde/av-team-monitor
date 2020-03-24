<template>
    <div class="tm-header-container">
        <v-toolbar
			app
			:color="[
					settings.darkTheme ? 'grey' : 'white',
					settings.darkTheme ? 'darken-4' : ''
			].join(' ')"
		>
			<div class="tm-toolbar-left no-overflow">

				<div 
					class="tm-logo current elevation-1 no-select"
					@click="toggleAvatars()"
				>
					<v-card
						:color="[
								!settings.darkTheme ? settings.primaryColor : settings.secondaryColor,
								currentTeam ? 'lighten-1' : ''
						].join(' ')"
					>
						<v-avatar
							tile
							class="mr-2 ml-1 mb-1 tm-logo-avatar"
							size="44px"
						>
							<picture>
								<source :srcset="`../static/images/logos/${teamLogo}-44.webp`" type="image/webp">
								<img :src="`../static/images/logos/${teamLogo}-44.png`" alt="teamLogo">
							</picture>
						</v-avatar>

						<span class="tm-logo-text white--text mr-5">{{ teamText }}</span>
						
						<v-icon 
							class="tm-logo-menu"
							:class="{'open': settings.avatars.enabled}"
							:dark="settings.darkTheme" 
							:light="!settings.darkTheme"
						>more_vert</v-icon>
					</v-card>
				</div>

			</div>
			
			<div class="tm-toolbar-right">

				<v-btn
					:outline="!noFilterSelected"
					:color="!noFilterSelected ? settings.primaryColor : ''"
					icon
					@click="openFilter()"
					class="mr-3 tm-filter-btn-lite"
					title="Filter"
				>
					<v-icon>filter_list</v-icon>
				</v-btn>

				<v-btn
					:outline="!noFilterSelected"
					:color="!noFilterSelected ? settings.primaryColor : ''"
					:icon="noFilterSelected"
					@click="openFilter()"
					class="mr-3 tm-filter-btn-full"
					title="Filter"
				>
					<v-icon>filter_list</v-icon>
					{{ !noFilterSelected ? 'filter active' : '' }}
				</v-btn>

				<v-btn
					icon
					@click="snackbar = true"
					class="mx-2 tm-search-btn"
					title="Search"
				>
					<v-icon>search</v-icon>
				</v-btn>

				<search-field :settings="settings" :search="search" inline/>

			    <v-snackbar
					v-model="snackbar"
					top
					auto-height
					:color="settings.darkTheme ? 'black' : 'white'"
				>
					<search-field :settings="settings" :search="search"/>
					
					<v-btn flat icon :color="!settings.darkTheme ? 'black' : 'white'" @click="snackbar = false" class="ml-2">
						close
					</v-btn>
				</v-snackbar>
				
				<v-btn 
					icon 
					:loading="refresh.auto || refresh.loading" 
					:disabled="refresh.auto || refresh.disabled" 
					@click="refreshTasks()"
					title="Refresh tasks"
				>
					<v-icon>{{ refresh.icon }}</v-icon>
				</v-btn>

				<v-badge 
					:color="[
						settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
						settings.darkTheme ? 'lighten-5' : 'accent-2'
					].join(' ')" 
					right
					overlap
					v-if="refresh.auto"
					class="tm-auto-refresh-badge"
				>
					<span 
						slot="badge"
						:class="!settings.darkTheme ? 'white--text' : 'black--text'"
						v-if="refresh.auto"
						title="Auto refresh is ON (every minute)"
					>A</span>
				</v-badge>
				
				<options-menu :settings="settings"></options-menu>
			</div>

			<filter-dialog :settings="settings" :filter="filter" :current-team="currentTeam"></filter-dialog>
        </v-toolbar>
    </div>
</template>

<script>
import {bus} from '../main';
import OptionsMenu from '../components/OptionsMenu.vue';
import SearchField from '../components/SearchField.vue';
import FilterDialog from '../components/FilterDialog.vue';

export default {
	components: {
		OptionsMenu,
		FilterDialog,
		SearchField
	},

	data() {
		return {
			search: '',

			refresh: {
				loading: false,
				icon: 'refresh',
				disabled: false,
				auto: false
			},

			snackbar: false
		};
	},

	computed: {
		teamLogo() {
			return this.currentTeam ? this.currentTeam.teamName : 'app-logo';
		},

		teamText() {
			return this.currentTeam ? this.currentTeam.caption + ' Team' : 'All Teams';
		},

		noFilterSelected() {
			return (
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
		}
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

		filter: {
			type: Object,
			required: true
		}
	},

	created() {
		bus.$on('tasksLoading', initial => {
			if (!initial) {
				this.refresh.loading = true;
				this.refresh.disabled = true;
			}
		});

		bus.$on('tasksLoaded', initial => {
			if (!initial) {
				let vm = this;
				setTimeout(_ => {
					vm.refresh.icon = 'check';
					vm.refresh.loading = false;
					setTimeout(_ => {
						vm.refresh.icon = 'refresh';
						vm.refresh.disabled = false;
					}, 500);
				}, 500);
			}
		});

		bus.$on('taskSearch', searchString => {
			this.search = searchString;
		});

		bus.$on('themeChanged', isDarkTheme => {
			this.darkTheme = isDarkTheme;
		});

		bus.$on('autoRefresh', state => {
			let vm = this;
			if (state) {
				vm.refresh.auto = true;
				if (!window.reloadInterval) {
					window.reloadInterval = setInterval(_ => {
						bus.$emit('refreshTasks');
					}, 60000);
				}
			} else {
				vm.refresh.auto = false;
				clearInterval(window.reloadInterval);
				window.reloadInterval = undefined;
			}
		});

		bus.$on('searchModified', val => {
			this.search = val;
		});
	},

	methods: {
		refreshTasks() {
			bus.$emit('refreshTasks');
			this.refresh.loading = true;
		},

		toggleAvatars() {
			bus.$emit('toggleAvatars');
		},

		autoRefreshTasks() {
			this.loading = !this.loading;
		},

		openFilter() {
			bus.$emit('openFilter');
		},

		toggleSearchRow() {
			this.$emit('toggleSearchRow');
		}
	},

	watch: {
		search() {
			bus.$emit('taskSearch', this.search);
		}
	}
};
</script>

<style>
.tm-header-container {
	height: 64px;
	margin-bottom: 10px;
}

.toolbar {
	height: inherit;
	z-index: 2;
}

.tm-team-exp-panel.closed {
	z-index: 1;
	margin-top: -3px;
}

.tm-team-exp-panel {
	width: 100%;
}

#tm-app > div.application--wrap > div.tm-header-container > ul > li > div.expansion-panel__header {
	padding: 0;
	padding-left: 10px;
}

#tm-app > div.application--wrap > div.tm-header-container > ul > li > div.expansion-panel__body > div > div {
	padding: 15px 5px 5px 5px;
}

#tm-app > div.application--wrap > div.tm-header-container > ul > li > div.expansion-panel__body > div > div > div {
	padding: 0;
}

#tm-app .tm-logo > div {
	line-height: 52px;
	padding: 0;
}

#tm-app .tm-logo-text {
	font-weight: bold;
	font-size: 20px;
}

#tm-app .tm-logo-avatar {
	padding: 0;
	margin: 0;
	border-radius: 5px;
	background: #f0f0f0;
	box-shadow: 1px 1px #d1d0d0;
}

#tm-app .tm-logo-avatar > picture {
	border-bottom-right-radius: 10px;
	height: 40px;
	width: 40px;
}

#tm-app .tm-logo {
	pointer-events: none;
}

#tm-app .tm-logo:hover {
	transform: scale(1.02);
}

#tm-app .tm-logo:hover .card {
	filter: brightness(1.1);
}

#tm-app .tm-logo:hover .tm-logo-avatar > picture {
	transform: scale(1.1);
}

#tm-app .tm-logo-menu {
	display: none;
}

#tm-app .tm-logo-menu.open {
	color: white;
}

.toolbar__content {
	height: 64px !important;
}

#tm-app .tm-search-divider {
	visibility: hidden;
}

#tm-app .tm-search-btn {
	display: none;
}

#tm-app .tm-toolbar-left {
	width: 30%;
	display: flex;
	justify-content: flex-start;
	align-items: center;
	margin-left: 5px;
}

#tm-app .tm-toolbar-right {
	display: flex;
	justify-content: flex-end;
	align-items: center;
	width: 70%;
}

#tm-app .tm-filter-btn-lite {
	display: none;
}

@media screen and (max-width: 767px) {
	#app > div.application--wrap > div.tm-header-container > nav > div > div.spacer {
		display: none;
	}

	#tm-app .tm-logo {
		margin-right: 20px;
		pointer-events: auto;
		cursor: pointer;
	}
}

@media screen and (max-width: 620px) {
	#tm-app .tm-logo > div {
		border-radius: 5px;
	}

	
	#tm-app .tm-logo-menu {
		display: inline;
	}

	#tm-app .tm-search-divider {
		display: none;
	}

	#tm-app .tm-logo {
		min-width: 85px;
	}

	.tm-filter-text {
		display: none;
	}

	#tm-app .tm-search-btn,
	#tm-app .tm-filter-btn-lite {
		display: block;
	}

	#tm-app .tm-logo-text,
	#tm-app .tm-filter-text,
	#tm-app .tm-filter-btn-full {
		display: none;
	}
}

@media screen and (max-width: 350px) {
	#tm-app .tm-logo-menu {
		display: none;
	}

	#tm-app .tm-logo {
		min-width: 50px;
	}
}

span.tm-auto-refresh-badge > span.badge__badge {
	top: -7px !important;
	right: 18px !important;
	width: 15px !important;
	height: 15px !important;
	cursor: default;
}
</style>
<template>
	<div class='tm-options-menu'>
		<v-menu
			:close-on-content-click="false"
			
			v-model="menu"
			origin="top right"
			bottom
			offset-y
		>
			<v-btn icon slot="activator" :dark="settings.darkTheme" :light="!settings.darkTheme" title="Options">
				<v-icon :dark="settings.darkTheme" :light="!settings.darkTheme">more_vert</v-icon>
			</v-btn>

			<v-card>
				<v-list dense :dark="!settings.darkTheme" :light="settings.darkTheme">
					<v-list-tile>
						<v-icon 
							:color="[
								settings.primaryColor,
								!settings.darkTheme ? 'lighten-5' : 'darken-2'
							].join(' ')"
						>settings_applications</v-icon>
						
						<v-list-tile-title class="subheading ml-2">Settings</v-list-tile-title>

						<v-btn flat icon :color="settings.darkTheme ? 'black' : 'white'" @click="menu = false" class="tm-options-close-btn ml-2">
							<v-icon>close</v-icon>
						</v-btn>
					</v-list-tile>
				</v-list>

				<v-divider></v-divider>
				
				<v-tabs
					v-model="activeTab"
					:slider-color="settings.primaryColor"
					height="48px"
					fixed-tabs
				>
					<v-tab>General</v-tab>
					<v-tab-item>
						<v-list>
							<v-list-tile class="my-1">
								<v-switch 
									v-model="darkTheme" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')"
									hint="switch page to dark/light theme"
									persistent-hint
								>
									<div slot="label" class="body-1">dark theme</div>
								</v-switch>
							</v-list-tile>

							<v-list-tile class="my-1">
								<v-switch 
									v-model="autoReload" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')"
									hint="auto reload table every minute"
									persistent-hint
								>
									<div slot="label" class="body-1">auto reload</div>
								</v-switch>
							</v-list-tile>
						</v-list>
					</v-tab-item>

					<v-tab>Avatars</v-tab>
					<v-tab-item>
						<v-list>
							<v-list-tile class="my-4">
								<v-badge 
									:color="[
										settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
										settings.darkTheme ? 'lighten-2' : 'ligthen-1'
									].join(' ')"
									right 										
									overlap
									:class="{'tm-avatar-tile': settings.avatars.tile}"
								>
									<span 
										slot="badge" 							
										:class="!settings.darkTheme ? 'white--text' : 'black--text'"
									>?</span>
										<v-avatar
											:tile="avatar.tile"
											:size="avatarSize"
											class="grey lighten-4 ml-3 tm-menu-avatar"
										>
											<img src="static/images/avatars/avatar_default.webp" alt="avatar">
										</v-avatar>
								</v-badge>
								<v-icon x-large class="mx-4">
									{{ avatar.vertical ? 'border_vertical' : 'border_horizontal'}}
								</v-icon>
							</v-list-tile>
							
							<v-list-tile class="my-4">
								<v-slider
									v-model="slider"
									:min="40"
									:max="70"
									hint="change the size of avatar"
									persistent-hint
									prepend-icon="photo_size_select_small"
									thumb-label
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')"
									:thumb-color="settings.primaryColor"
								></v-slider>
							</v-list-tile>
							
							<v-list-tile class="my-2">
								<v-switch 
									v-model="avatar.enabled" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									label="enabled"
									hint="enable/disable avatar panel"
									persistent-hint

								>
									<div slot="label" class="body-1">enabled</div>
								</v-switch>
							</v-list-tile>

							<v-list-tile class="my-2">
								<v-switch 
									v-model="avatar.tile" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									label="tile"
									hint="change avatar form - round/rectangle"
									persistent-hint
								>
									<div slot="label" class="body-1">tile</div>
								</v-switch>
							</v-list-tile>

							<v-list-tile class="my-2">
								<v-switch 
									v-model="avatar.vertical" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									hint="change avatars direction - horizontal/vertical"
									persistent-hint
								>
									<div slot="label" class="body-1">vertical</div>
								</v-switch>
							</v-list-tile>

							<v-list-tile class="my-2">
								<v-switch 
									v-model="avatar.filterNoTasks" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									hint="filter out members without tasks"
									persistent-hint
								>
									<div slot="label" class="body-1">filter no tasks</div>
								</v-switch>
							</v-list-tile>
						</v-list>
					</v-tab-item>
					
					<v-tab>Table</v-tab>
					<v-tab-item>
						<v-list>
							<v-list-tile class="my-2">
								<v-switch 
									v-model="table.typeIcon" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									hint="show type icon in front of jira key"
									persistent-hint
								>
									<div slot="label" class="body-1">type icon</div>
								</v-switch>
							</v-list-tile>

							<v-list-tile class="my-2">
								<v-switch 
									v-model="table.teamLogo" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									hint="show team logo instead of label"
									persistent-hint
								>
									<div slot="label" class="body-1">team logo</div>
								</v-switch>
							</v-list-tile>

							<v-list-tile class="my-2">
								<v-switch 
									v-model="table.statusLabel" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									hint="show status value as label"
									persistent-hint
								>
									<div slot="label" class="body-1">status label</div>
								</v-switch>
							</v-list-tile>
							
							<v-list-tile class="my-2">
								<v-switch 
									v-model="table.priorityIcon" 
									:color="[
										settings.primaryColor,
										'ligthen-1'
									].join(' ')" 
									hint="show priority as icon"
									persistent-hint
								>
									<div slot="label" class="body-1">priority icon</div>
								</v-switch>
							</v-list-tile>
						</v-list>
					</v-tab-item>
				</v-tabs>
			</v-card>
		</v-menu>
	</div>
</template>

<script>
import { bus } from '../main';
export default {
	data() {
		return {
			activeTab: null,

			drawer: false,

			menu: false,

			slider: parseInt(this.settings.avatars.size),

			avatar: {
				size: this.settings.avatars.size,
				tile: this.settings.avatars.tile,
				vertical: this.settings.avatars.vertical,
				enabled: this.settings.avatars.enabled,
				filterNoTasks: this.settings.avatars.filterNoTasks
			},

			darkTheme: this.settings.darkTheme,

			autoReload: this.settings.autoReload,

			table: {
				typeIcon: this.settings.table.typeIcon,
				priorityIcon: this.settings.table.priorityIcon,
				statusLabel: this.settings.table.statusLabel,
				teamLogo: this.settings.table.teamLogo
			}
		};
	},

	props: {
		settings: {
			type: Object,
			required: true
		}
	},

	computed: {
		avatarSize() {
			return `${this.slider}px`;
		}
	},

	watch: {
		avatar: {
			handler() {
				bus.$emit('settingAvatarsChanged', this.avatar);
			},
			deep: true
		},

		autoReload() {
			bus.$emit('autoRefresh', this.autoReload);
		},

		darkTheme() {
			bus.$emit('settingDarkThemeChanged', this.darkTheme);
		},

		slider() {
			this.avatar.size = `${this.slider}px`;
		},

		table: {
			handler() {
				bus.$emit('settingTableChanged', this.table);
			},
			deep: true
		}
	}
};
</script>

<style>
.badge > div.avatar.tm-menu-avatar,
.badge.tm-avatar-tile > div.avatar.tm-menu-avatar > avatar--tile {
	min-width: 40px;
	min-height: 40px;
}

div.tm-menu-avatar.avatar--tile {
	border-radius: 5px;
}

.tm-options-close-btn {
	visibility: hidden;
	min-width: 36px;
}

@media screen and (max-width: 620px) {
	.tm-options-close-btn {
		visibility: visible;
	}
}
</style>

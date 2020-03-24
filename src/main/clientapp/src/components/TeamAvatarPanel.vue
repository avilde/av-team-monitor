<template v-if="teams">
	<v-card 
		flat 
		class="tm-avatar-panel" 
		transition="slide-x-reverse-transition"
		:style="{
			'max-width': settings.avatars.vertical ? '150px' : '100%',
			'min-width': settings.avatars.vertical ? '100%' : settings.avatars.size
		}"
	>
		<v-card-text 
			class="tm-avatar-card tm-scrollbar"
			:class="{
				'dark': settings.darkTheme,
				'light': !settings.darkTheme
			}"
		>
			{{ currentTeam ? 'Total' : 'Teams' }}

			<div
				:class="[
					'tm-avatar-teams',
					settings.avatars.vertical ? 'vertical' : ''
				]"
			>
				<v-chip 
					v-if="!currentTeam"
					:color="[
						settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
					].join(' ')"
					:class="{
						'tm-avatar-tile': settings.avatars.tile,
						'elevation-5': true,
						'tm-team-chip-container': true
					}"
				>
					<span class="tm-team-chip total">{{ currentTeam && visibleMembers.length === 0 ? 0 : totalTaskCount || '?' }}</span>
				</v-chip>

				<div 
					:class="[
						'tm-team-avatar-container',
						currentTeam ? '' : (settings.avatars.vertical ? 'my-1' : 'mx-1')
					]"
					:key="team.rowId" 
					v-for="team in visibleTeams"
				>
					<v-badge 
						:color="[
							settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
							settings.darkTheme ? 'lighten-5' : 'accent-2'
						].join(' ')" 
						right 										
						overlap
						:class="{'tm-avatar-tile': settings.avatars.tile}"
						v-if="!currentTeam ? true : currentTeam.teamName === team.teamName"
					>
						<span 
							slot="badge"
							:class="!settings.darkTheme ? 'white--text' : 'black--text'"
							v-if="!currentTeam"
						>{{ team.taskCount }}</span>

						<v-chip 
							@click="selectTeam(team.teamName, team.caption)"
							:color="[
								settings.secondaryColor,
								currentTeam ? 'accent-5' : (settings.darkTheme ? 'lighten-2' : 'lighten-4')
							].join(' ')"
							:close="currentTeam && currentTeam.teamName === team.teamName"
							@input="unSelectTeam()"
							:class="{
								'tm-avatar-chip': true,
								'vertical': settings.avatars.vertical,
								'tm-avatar-tile': settings.avatars.tile,
								'white--text': settings.darkTheme,
								'black--text': !settings.darkTheme,
								'elevation-5': currentTeam,
								'elevation-2': !currentTeam
							}"
						>
							<v-avatar v-if="!currentTeam">
								<picture>
									<source :srcset="`../static/images/logos/${team.teamName}-32.webp`" type="image/webp">
									<img :src="`../static/images/logos/${team.teamName}-32.png`" :alt="team.teamName">
								</picture>
							</v-avatar>
							
							<span 
								v-if="!currentTeam"
								class="tm-team-chip"
							>{{ team.caption }}</span>

							<span 
								v-if="currentTeam"
								class="tm-team-chip total"
							>{{ team.taskCount }}</span>
						</v-chip>
					</v-badge>
				</div>

				<div
					v-if="currentTeam && visibleMembers"
					:class="[
						'tm-team-members',
						settings.avatars.vertical ? 'tm-scrollbar': '',
						settings.avatars.vertical ? 'vertical' : ''
					]"
				>
					<div 
						:class="[
							'tm-member-container',
							settings.avatars.vertical ? 'my-4' : 'mx-3'
						]"
						:key="member.rowId"
						v-for="member in visibleMembers"	
					>
						<v-badge 
							:color="[
								settings.darkTheme ? 
									(currentMember && currentMember.rowId === member.rowId ?
										settings.secondaryColor : 
										settings.primaryColor
									) :
									(currentMember && currentMember.rowId === member.rowId ?
										settings.secondaryColor : 
										settings.primaryColor	
									),
								currentMember && currentMember.rowId === member.rowId ? 'accent-3' : (settings.darkTheme ? 'lighten-2' : 'ligthen-1')
							].join(' ')"  
							right 										
							overlap
							:class="{
								'tm-avatar-tile': settings.avatars.tile,
								'elevation-2': true,
								'selected': currentMember && currentMember.rowId === member.rowId
							}"
						>
							<span 
								slot="badge"
								:class="{
									'white--text': !settings.darkTheme && currentMember && currentMember.rowId === member.rowId,
									'black--text': settings.darkTheme && currentMember && currentMember.rowId === member.rowId,
									'selected': currentMember && currentMember.rowId === member.rowId
								}"
							>{{ member.taskCount }}</span>

							<v-avatar 
								:size="settings.avatars.size"
								:tile="settings.avatars.tile"
								:class="{
									'tm-member-avatar': true,
									'selected' : currentMember && currentMember.rowId === member.rowId
								}"
								@click="toggleSelected(member)"
							>
								<avatar-picture
									v-if="currentTeam"
									:settings="settings" 
									:username="member.login"
									:current-username="currentMember && currentMember.login"
								></avatar-picture>

								<span 
									:class="[
										'tm-nickname',
										settings.avatars.vertical ? 'vertical' : ''
									]"
									v-if="currentMember && currentMember.rowId === member.rowId"
								>{{ member.nickName }}</span>
							</v-avatar>
						</v-badge>
					</div>
				</div>
			</div>
		</v-card-text>
	</v-card>
</template>

<script>
import { bus } from '../main';
import AvatarPicture from './AvatarPicture.vue';

export default {
	components: {
		'avatar-picture': AvatarPicture
	},

	props: {
		teams: {
			type: Object
		},

		settings: {
			type: Object
		},

		vertical: {
			type: Boolean,
			default: false
		},

		currentTeam: {
			type: Object,
			default: undefined
		},

		currentMember: {
			type: Object,
			default: undefined
		}
	},

	computed: {
		visibleTeams() {
			let teams = Object.values(this.teams).sort((a, b) => {
				return a.taskCount > b.taskCount ? -1 : 1;
			});
			if (this.settings.avatars.filterNoTasks)
				teams = teams.filter(team => team.taskCount && team.taskCount > 0);
			return teams;
		},

		visibleMembers() {
			let members = Object.values(this.currentTeam.members).sort((a, b) => {
				return a.taskCount > b.taskCount ? -1 : 1;
			});

			if (this.settings.avatars.filterNoTasks)
				members = members.filter(member => member.taskCount && member.taskCount > 0);

			return members;
		},

		totalTaskCount() {
			let total = 0;

			for (const t in this.teams)
				total += this.teams[t].taskCount;

			return total;
		}
	},

	methods: {
		selectTeam(teamName) {
			bus.$emit('teamSelected', this.teams[teamName]);
		},

		unSelectTeam() {
			bus.$emit('teamSelected');
		},

		selectMember(member) {
			bus.$emit('memberSelected', member);
		},

		unselectMember() {
			bus.$emit('memberSelected');
		},

		toggleSelected(memb) {
			this.currentMember && memb === this.currentMember ? this.unselectMember() : this.selectMember(memb);
		}
	}
};
</script>

<style>
div.tm-avatar-teams {
	display: flex;
}

div.tm-avatar-teams.vertical {
	flex-direction: column;
}

div.tm-team-members {
	display: flex;
	max-height: 70px;
}

div.tm-team-members.vertical {
	overflow-x: hidden;
	overflow-y: auto;
	max-height: calc(100vh - 200px);
	padding-right: 20px;
	padding-left: 10px;
	flex-direction: column;
}

div.tm-avatar-panel {
	margin-top: -10px;
	user-select: none;
}

span.tm-avatar-chip.vertical {
	margin: 10px 0;
	user-select: none;
}

span.tm-avatar-chip:not(.chip--removable) > span.chip__content:hover {
	cursor: pointer;
}

.tm-avatar-tile,
.tm-avatar-tile img,
.badge.badge--overlap.tm-avatar-tile > .badge__badge {
	border-radius: 4px !important;
	user-select: none;
	transition: border-radius 0.2s cubic-bezier(0.32, 0.46, 0.45, 0.94);
}

.badge.badge--overlap:not(.tm-avatar-tile) {
	border-radius: 50%;
	transition: border-radius .3s cubic-bezier(0.075, 0.82, 0.165, 1);
}

.tm-team-chip-container {
	max-width: 70px;
}

span.tm-team-chip {
	color: #000;
}

span.tm-team-chip.total {
	color: rgba(325,325,325,.79);
	font-size: 20px;
	font-weight: bold;
	user-select: none;
	text-shadow: 1px 0px rgba(0,0,0,.09);
}

.chip--active:not(.chip--disabled):after, .chip--selected:not(.chip--disabled):after, .chip:focus:not(.chip--disabled):after {
	background: none;
}

div.tm-member-avatar {
	transition: scale 2s linear;
}

div.tm-member-avatar.selected > img {
	transform: scale(1.15);
	filter: contrast(1.3);
}

div.tm-member-avatar.selected:not(.avatar--tile.tm-avatar-tile),
div.tm-member-avatar.selected:not(.avatar--tile.tm-avatar-tile) > img {
	border-radius: 10px;
}

div.tm-member-avatar.selected.tm-avatar-tile.avatar--tile,
div.tm-member-avatar.selected.tm-avatar-tile.avatar--tile > img {
	border-radius: 50%;
}

.badge.badge--overlap.selected > span.badge__badge {
	transform: scale(1.15);
}

div.tm-member-avatar:hover {
	cursor: pointer;
	opacity: .9;
	transform: scale(1.02);
	filter: grayscale();
}

div.tm-member-avatar:active {
	opacity: .7;
}

span.tm-nickname {
	font-size: 10px;
	position: absolute;
	top: -20px;
	left: 0;
	text-align: left;
	width: max-content;
}

.tm-avatar-card {
	overflow-x: auto;
}

@media screen and (max-width: 767px) {
	.tm-avatar-panel-container {
		position: fixed;
		top: 65px;
		left: 10px;
		z-index: 1;
	}
}

picture {
	height: inherit;
	width: inherit;
}
</style>

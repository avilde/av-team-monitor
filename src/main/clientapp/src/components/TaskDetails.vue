<template>
  <v-dialog
	v-if="currentItem"
	v-model="open"
	content-class="tm-task-dialog tm-scrollbar"
	@keydown.esc="open = false"
>
	<v-card>
		<v-card-title 
			:class="[
				!settings.darkTheme ? 'white--text' : 'black--text',
				settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
				!settings.darkTheme ? 'accent-5' : 'lighten-3'
			]"
		>
			<v-flex align-content-start class="tm-headline no-overflow">
				<span>{{ currentItem.summary }}</span>
			</v-flex>
			<v-flex class="tm-close-btn-flex" align-content-end>
				<v-btn flat icon :color="settings.darkTheme ? 'black' : 'white'" @click="open = false">
					<v-icon>close</v-icon>
				</v-btn>
			</v-flex>
		</v-card-title>

		<v-card-text>
			<v-container fluid wrap class="tm-details-container tm-scrollbar">
				<v-layout v-if="parent && currentItem.isSubtask" class="tm-parent-container">
						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Parent: </span>

							<span class="tm-field-value subheading">
								<a
									target="_blank"
									rel="noopener"
									:href="getJiraItemUrl(parent)"
									:class="{
										'tm-details-jira-url': true,
										'dark': settings.darkTheme
									}"
								> {{ parent.itemKey }}</a>
								{{ parent.summary }}
							</span>
						</v-flex>
				</v-layout>

				<span class="horizontal-delimit" v-if="parent"></span>

				<v-layout row wrap class="tm-task-dialog-fields">
					<v-flex class="tm-field-group">
						<v-flex class="tm-task-field" v-if="sprints && sprints.length > 0">
							<span class="tm-field-label title"> Sprints: </span>
							<span class="tm-label" v-for="sprint in sprints" :key="sprint.name" :sprint-state="sprint.state">
								{{ sprint.name.toUpperCase() }}
							</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Task #: </span>
							<span class="tm-field-value subheading">
								<a
									v-if="isJira"
									target="_blank"
									rel="noopener"
									:href="getJiraItemUrl(currentItem)"
									:class="{
										'tm-details-jira-url': true,
										'dark': settings.darkTheme
									}"
								> {{ currentItem.itemKey }}</a>
								<template v-else>{{ currentItem.itemKey }}</template>
							</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Type: </span>
							<span class="tm-field-value subheading no-overflow">
								<span class="type-icon">
									<fa-icon 
										:style="{color: getTypeIcon(currentItem.type).color}"
										:icon="getTypeIcon(currentItem.type).icon"
									></fa-icon>
								</span>
								<span class="type-text">{{ currentItem.type.toLowerCase() }}</span>
							</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Status: </span>
							<span class="tm-field-value subheading no-overflow">
								<span 
									class="status"
									:status="currentItem.status.toLowerCase()"
								>{{ currentItem.status }}</span>
							</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Priority: </span>
							<span class="tm-field-value subheading">
								<v-icon 
									class="priority-icon" 
									:priority="currentItem.priority.toLowerCase()"
								>{{ priorityIcon }}</v-icon>
								<span class="priority-text">{{ currentItem.priority }}</span>
							</span>
						</v-flex>
					</v-flex>

					<v-flex class="tm-field-group">
						<v-flex class="tm-task-field" v-if="labels && labels.lenght > 0">
							<span class="tm-field-label title"> Labels: </span>
							<span class="tm-label" v-for="(label, idx) in labels" :key="idx">
								{{ label.toUpperCase() }}
							</span>
						</v-flex>

						<v-flex class="tm-task-field team">
							<span class="tm-field-label title"> Team: </span>

								<v-avatar 
									size="25px"
									tile
									class="tm-details-avatar tm-avatar-tile"
								>
									<picture>
										<source :srcset="`../static/images/logos/${currentItem.team}-25.webp`" type="image/webp">
										<img :src="`../static/images/logos/${currentItem.team}-25.png`" :alt="currentItem.team">
									</picture>
								</v-avatar>
								<span class="tm-details-avatar-text">{{ currentItem.team }}</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Assignee: </span>

								<v-avatar 
									size="25px"
									tile
									class="tm-details-avatar"
								>
									<avatar-picture
										v-if="open"
										:settings="settings" 
										:username="currentItem.assignee"
										:size="'25'"
									></avatar-picture>								
								</v-avatar>
								<span class="tm-details-avatar-text">{{ currentItem.assignee }}</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title" v-if="isQc"> Created By: </span>
							<span class="tm-field-label title" v-if="isJira"> Reporter: </span>
							<v-avatar 
								size="25px"
								tile
								class="tm-details-avatar"
							>
									<avatar-picture
										v-if="open"
										:settings="settings" 
										:username="reporter"
										:size="'25'"
									></avatar-picture>									
							</v-avatar>
							<span class="tm-details-avatar-text">{{ reporter }}</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Created: </span>
							<span class="tm-field-value subheading">{{ moment(currentItem.created ? currentItem.created : currentItem.createdDate).fromNow() }}</span>
						</v-flex>

						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Last modified: </span>
							<span class="tm-field-value subheading">{{ moment(currentItem.updated).fromNow() }}</span>
						</v-flex>
					</v-flex>

					<span class="horizontal-delimit" v-if="currentItem.description"></span>

					<v-layout col wrap v-if="currentItem.description">
						<v-flex class="tm-task-field">
							<span class="tm-field-label title"> Description: </span>
							
							<div class="tm-field-value subheading description tm-scrollbar"><span v-html="jiraToHtml(currentItem.description)"></span></div>
						</v-flex>
					</v-layout>
					
					<v-flex class="tm-field-group">
						<v-flex class="tm-task-field">
							<span class="tm-field-label subheading details" v-if="environment && environment.length > 1"> Environment: </span>
							<span class="tm-field-value subheading">{{ environment }}</span>
						</v-flex>

						<v-flex class="tm-task-field" v-if="currentItem.testData && currentItem.testData.length > 1">
							<span class="tm-field-label subheading details"> Test Data: </span>
							<span class="tm-field-value subheading">{{ currentItem.testData }}</span>
						</v-flex>

						<v-flex class="tm-task-field" v-if="currentItem.reproduceSteps && currentItem.reproduceSteps.length > 1">
							<span class="tm-field-label subheading details"> Steps To Reproduce: </span>
							<span class="tm-field-value subheading pre"><span v-html="'\n' + jiraToHtml(currentItem.reproduceSteps)"></span></span>
						</v-flex>

						<v-flex class="tm-task-field" v-if="currentItem.expectedResult && currentItem.expectedResult.length > 1">
							<span class="tm-field-label subheading details"> Expected Result: </span>
							<span class="tm-field-value subheading">{{ currentItem.expectedResult }}</span>
						</v-flex>

						<v-flex class="tm-task-field" v-if="currentItem.actualResult && currentItem.actualResult.length > 1">
							<span class="tm-field-label subheading details"> Actual Result: </span>
							<span class="tm-field-value subheading">{{ currentItem.actualResult }}</span>
						</v-flex>
					</v-flex>
				</v-layout>

				<span class="horizontal-delimit" v-if="comments && comments.length > 0"></span>

				<v-layout col wrap>
					<v-flex class="tm-task-field" v-if="comments && comments.length > 0">
						<span class="tm-field-label title"> Comments: </span>
						<div class="tm-field-value subheading tm-scrollbar">
							<div class="tm-comments-container">
								<div class="tm-comment" v-for="comment in comments" :key="comment.id">
									<div
										:class="[
											'tm-comment-header',
											!settings.darkTheme ? 'white--text' : 'black--text',
											settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
											!settings.darkTheme ? 'accent-5' : 'lighten-3'
										]"
									>
										<span class="tm-comment-user">{{ comment.user }}</span>
										<span class="tm-comment-date">{{ moment(comment.updatedDate).fromNow() }}</span>
									</div>
									<div class="tm-comment-body"><span v-html="jiraToHtml(comment.body)"></span></div>
								</div>
							</div>
						</div>
					</v-flex>
				</v-layout>	

				<span class="horizontal-delimit" v-if="attachments && attachments.length > 0"></span>

				<v-layout col wrap>
					<v-flex class="tm-task-field" v-if="attachments && attachments.length > 0">
						<span class="tm-field-label title"> Attachments: </span>
						<div class="tm-field-value subheading tm-scrollbar">
							<div class="tm-attachment-container">
								<div class="tm-attachment" v-for="att in attachments" :key="att.filename">
									<a 
										:class="[
											'tm-attachment-link',
											settings.darkTheme ? 'white--text' : 'black--text'
										]"
										:href="att.contentUri" 
										target="_blank"
									>
										<div class="tm-attachment-header no-overflow">
											<span class="tm-attachment-user">{{ att.user }}</span>
											<span class="tm-attachment-date">{{ moment(att.createdDate).fromNow() }}</span>
											<span class="tm-attachment-size">{{ humanFileSize(att.size) }}</span>
										</div>

										<div class="tm-attachment-body">
											<div class="attachment-thumb" v-if="att.thumbnailUri && att.thumbnailUri != 'null'">
												<img :src="att.thumbnailUri">
											</div>
											<div v-else class="tm-attachment-icon">
												<fa-icon size="lg" :icon="getAttachmentIcon(att)" class="attachment-icon" color="lightgrey"/>
												<span>{{ getAttachmentLabel(att) }}</span>
											</div>
										</div>
										
										<div class="tm-attachment-footer no-overflow">
											<span class="tm-attachment-file" :title="att.filename">{{ att.filename }}</span>
										</div>
									</a>
								</div>
							</div>
						</div>
					</v-flex>
				</v-layout>	
			</v-container>
		</v-card-text>
		<v-card-actions class="tm-details-actions">
			<v-btn color="primary" flat @click.stop="open = false">Close</v-btn>
		</v-card-actions>
	</v-card>
	</v-dialog>
</template>

<script>
import {bus} from '../main';
import moment from 'moment';
import AvatarPicture from './AvatarPicture.vue';
import {ATTACHMENT_ICONS, ATTACHMENT_LABELS, jiraToHtml, PRIO_ICONS, TYPE_ICONS} from '../globals';

export default {
	data() {
		return {
			open: false
		};
	},

	props: {
		settings: {
			type: Object,
			required: true
		},

		currentItem: {
			type: Object,
			default: undefined
		}
	},

	computed: {
		reporter() {
			return this.currentItem.createdBy ? this.currentItem.createdBy : this.currentItem.reporter;
		},

		isQc() {
			return this.currentItem.internalType === 'QC';
		},

		isJira() {
			return this.currentItem.internalType === 'JIRA';
		},

		priorityIcon() {
			return PRIO_ICONS[this.currentItem.priority.toLowerCase()];
		},

		comments() {
			return this.currentItem.comments ? JSON.parse(this.currentItem.comments) : [];
		},

		attachments() {
			return this.currentItem.attachments ? JSON.parse(this.currentItem.attachments) : [];
		},

		labels() {
			return this.currentItem.labels ? JSON.parse(this.currentItem.labels) : [];
		},

		sprints() {
			let sprints = [];
			if (this.currentItem.sprints) sprints = JSON.parse(this.currentItem.sprints).find(sprint => sprint.state !== 'CLOSED');

			return sprints;
		},

		parent() {
			return this.currentItem.parent ? JSON.parse(this.currentItem.parent) : undefined;
		},

		environment() {
			return this.currentItem.environments ? JSON.parse(this.currentItem.environments)['value'] : '';
		}
	},

	created() {
		bus.$on('openTaskDetails', _ => {
			this.open = true;
		});
	},

	methods: {
		moment(...args) {
			return moment(...args);
		},

		getInitials(username) {
			return username.slice(0, 1) + username.slice(4)[0];
		},

		getTypeIcon(type) {
			return TYPE_ICONS.hasOwnProperty(type.toLowerCase()) ? TYPE_ICONS[type.toLowerCase()] : TYPE_ICONS['default'];
		},

		humanFileSize(size) {
			let i = Math.floor(Math.log(size) / Math.log(1024));
			return (size / Math.pow(1024, i)).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
		},

		getAttachmentIcon(att) {
			let ext = att && att.filename ? att.filename.split('.').pop() : undefined;

			return ext && ATTACHMENT_ICONS.hasOwnProperty(ext) ? ATTACHMENT_ICONS[ext] : ATTACHMENT_ICONS['default'];
		},

		getAttachmentLabel(att) {
			let ext = att && att.filename ? att.filename.split('.').pop() : undefined;

			return ext && ATTACHMENT_LABELS.hasOwnProperty(ext) ? ATTACHMENT_LABELS[ext] : ext;
		},

		getJiraItemUrl(item) {
			return `${this.settings.jiraHost}/browse/${item.itemKey}`;
		},

		jiraToHtml(...args) {
			return jiraToHtml(...args);
		}
	},

	components: {
		'avatar-picture': AvatarPicture
	}
};
</script>

<style>
.tm-task-dialog {
	width: auto !important;
	border-radius: 5px;
}

.tm-task-field {
	margin: 5px 0;
}

.tm-field-label {
	display: inline-flex;
	min-width: 150px;
}

.tm-task-dialog-fields {
	flex-direction: column;
}

.tm-field-value.description {
	display: inline-flex;
	flex-direction: column;
	min-height: 100px;
	overflow-y: auto;
	white-space: pre-wrap;
	width: -webkit-fill-available;
}

span.tm-nickname.right {
	font-size: 16px;
	position: absolute;
	top: 0;
	left: 40px;
	text-align: left;
}

.avatar--tile img.tm-details-avatar-img {
	border-radius: 3px;
}

a.tm-details-jira-url.dark {
	color: white;
}

a.tm-details-jira-url:hover {
	color: #ff9800;
}

.tm-close-btn-flex {
	text-align: right;
	display: none;
	flex-direction: row-reverse;
}

.tm-headline {
	font-size: 22px;
}

.tm-details-container {
	margin: 5px;
	padding: 0;
}

.tm-details-avatar:hover + span {
	visibility: visible;
}

.tm-details-avatar-text {
	visibility: hidden;
}

span.priority-text {
	visibility: hidden;
	font-size: 14px;
	text-transform: lowercase;
}

i.priority-icon:hover + span.priority-text {
	visibility: visible;
}

span.type-text {
	visibility: hidden;
	font-size: 14px;
	text-transform: lowercase;
}

span.type-icon:hover + span.type-text {
	visibility: visible;
}

div.tm-comments-container {
	display: flex;
	flex-direction: column;
}

div.tm-comment {
	display: flex;
	flex-direction: column;
	margin-bottom: 15px;
}

div.tm-comment-header {
	font-size: 13px;
	border-radius: 3px;
	filter: saturate(0.9);
	max-width: 180px;
}

div.tm-comment-header span {
	margin: 2px 10px;
}

div.tm-comment-body {
	font-size: 12px;
	margin: 5px;
}

div.tm-attachment-container {
	display: flex;
}

div.tm-attachment {
	max-width: 200px;
	text-overflow: hidden;
	white-space: nowrap;
	margin-right: 30px;
}

a.tm-attachment-link {
	text-decoration: none;
	font-size: 12px;
	color: black;
}

a.tm-attachment-link:hover {
	color: #319bd8 !important;
}

a.tm-attachment-link:hover div.tm-attachment-body {
	border-color: #a7defd;
	background-color: #f8f8f8;
}

div.tm-attachment-header {
	font-size: 10px;
	display: flex;
	justify-content: space-between;
}

div.tm-attachment-footer {
	font-size: 12px;
}

div.tm-attachment-body {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 200px;
	height: 150px;
	overflow: hidden;
	margin-bottom: 5px;
	border: 1px solid lightgray;
	border-radius: 3px;
}

div.tm-attachment-icon {
	display: flex;
	flex-direction: column;
	font-size: 20px;
	align-items: center;
}

i.attachment-icon {
	color: lightgray;
}

div.tm-attachment-icon > span {
	align-self: center;
	color: lightgray;
	margin-top: 6px;
}

.tm-label {
	border-style: solid;
	border-radius: 3px;
	border-width: 1px;
	padding: 2px 5px;
	margin-right: 10px;
	font-size: 11px;
	font-weight: bold;
}

.details {
	font-weight: bold;
}

.pre {
	white-space: pre-wrap;
}

span.horizontal-delimit {
	width: 100%;
	border-bottom: 1px solid lightgrey;
	margin: 10px 0;
	display: inherit;
}
@media screen and (max-width: 620px) {
	.tm-table-team-avatar,
	.tm-details-actions {
		display: none;
	}

	.tm-headline {
		font-size: 20px;
		width: 60vw;
	}

	.tm-close-btn-flex {
		display: flex;
	}

	.tm-field-value.description {
		max-height: 200px;
		word-break: break-word;
	}
	
	.tm-attachment-container {
		flex-wrap: wrap;
	}
}
</style>

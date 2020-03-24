<template>
	<div class="tm-avatar-picture">
		<img
			:src="imageUrl(username)"
			:class="{'tm-avatar-tile': settings.avatars.tile}"
			:alt="`${username} avatar`"
			:title="username"
		>
	</div>
</template>

<script>
import {VUETIFY_COLORS} from '../globals';

export default {
	props: {
		username: {
			type: String,
			required: true
		},

		settings: {
			type: Object,
			required: true
		},

		currentUsername: {
			type: String,
			required: false
		},

		size: {
			type: String,
			required: false
		}
	},

	methods: {
		getInitials(username) {
			return (username.slice(0, 1) + username.slice(4)[0]).toLowerCase();
		},

		imageUrl(username) {
			return `https://ui-avatars.com/api/?name=${this.getInitials(username)}&size=${this.size ? this.size : this.settings.avatars.size}&background=${this.getBackgroundColor()}&color=${this.getForegroundColor()}`;
		},

		getBackgroundColor() {
			return (VUETIFY_COLORS[this.settings.darkTheme || this.currentUsername === this.username ? this.settings.primaryColor : this.settings.secondaryColor].base).substring(1);
		},

		getForegroundColor() {
			return 'FFFFFF';
		}
	}
};
</script>

<style>
.tm-avatar-picture {
	height: inherit;
	width: inherit;
}
</style>

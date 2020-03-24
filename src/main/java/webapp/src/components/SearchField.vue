<template>
	<v-text-field
		class="tm-search-input"
		:class="inline ? 'inline' : ''"
		append-icon="search"
		label="Search"
		clearable
		v-model="searchVal"
		:color="settings.darkTheme ? 'white' : 'black'"
	></v-text-field>
</template>

<script>
import {bus} from '../main';
export default {
	data() {
		return {
			searchVal: this.search
		};
	},

	props: {
		settings: {
			type: Object,
			required: true
		},

		search: {
			required: true
		},

		inline: {
			type: Boolean,
			required: false,
			default: false
		}
	},

	watch: {
		searchVal() {
			bus.$emit('searchModified', this.searchVal);
		},

		search() {
			this.searchVal = this.search;
		}
	}
};
</script>

<style>
#tm-app .tm-search-input {
	max-width: 500px;
	min-width: 80px;
}

@media screen and (max-width: 620px) {
	#tm-app .tm-search-input.inline {
		display: none;
	}
}
</style>

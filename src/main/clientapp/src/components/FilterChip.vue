<template>
	<div
		@click="toggle(label)"
		:class="[
			'tm-chip',
			selected ? settings.primaryColor : settings.secondaryColor,
			selected ? ' lighten-3' : ' lighten-5',
			'black--text',
			selected ? 'selected' : ''
		].join(' ')"
	>						
		<span class="tm-chip-content">{{ label }}</span>

		<span 
			:class="[
				'tm-chip-plus',
				selected ? 'selected' : ''
			
			]"
		>
			<fa-icon
				:style="{color: colorCalc}"
				icon="plus-circle"
				size="lg"
			/>
		</span>
	</div>
</template>

<script>
import {VUETIFY_COLORS} from '../globals';
import {bus} from '../main';

export default {
	data() {
		return {
			selected: this.filter[this.type].indexOf(this.label) > -1
		};
	},

	props: {
		settings: {
			type: Object,
			required: true
		},

		label: {
			type: String,
			required: true
		},

		type: {
			type: String,
			required: true
		},

		filter: {
			type: Object,
			required: true
		}
	},

	computed: {
		colorCalc() {
			return this.selected ? this.colors('red', 'accent-3') : this.colors('green', 'lighten-3');
		}
	},

	created() {
		bus.$on('clear-filters', this.unSelect);
	},

	methods: {
		toggle(label) {
			this.selected = !this.selected;
			bus.$emit('filter-selected', {type: this.type, label: this.label});
		},

		colors(color, tone) {
			return VUETIFY_COLORS[color][tone];
		},

		unSelect() {
			this.selected = false;
		}
	}
};
</script>

<style scoped>
.tm-chip {
	-webkit-box-align: center;
	-ms-flex-align: center;
	align-items: center;
	border-radius: 28px;
	border: 1px solid #0000;
	display: -webkit-inline-box;
	display: -ms-inline-flexbox;
	display: inline-flex;
	font-size: 14px;
	margin: 10px;
	outline: none;
	position: relative;
	-webkit-transition: 0.3s cubic-bezier(0.25, 0.8, 0.5, 1);
	transition: 0.3s cubic-bezier(0.25, 0.8, 0.5, 1);
	vertical-align: middle;
	cursor: pointer;
}

.tm-chip.selected {
	border-radius: 3px;
}

.tm-chip:hover {
	text-shadow: 1px 0 black;
}

.tm-chip:hover {
	text-shadow: 1px 0 black;
}

.tm-chip-content {
	-webkit-box-align: center;
	-ms-flex-align: center;
	align-items: center;
	border-radius: 28px;
	cursor: default;
	display: -webkit-inline-box;
	display: -ms-inline-flexbox;
	display: inline-flex;
	height: 32px;
	-webkit-box-pack: justify;
	-ms-flex-pack: justify;
	justify-content: space-between;
	padding: 0 12px;
	vertical-align: middle;
	white-space: nowrap;
	z-index: 1;
	cursor: pointer;
}

.tm-chip-plus {
	margin: 5px;
	width: 17px;
	height: 17px;
	line-height: 17px;
	-webkit-transition: 0.3s cubic-bezier(0.25, 0.8, 0.5, 1);
	transition: 0.3s cubic-bezier(0.25, 0.8, 0.5, 1);
}

.tm-chip-plus.selected {
	transform: rotate(45deg);
	transform-origin: center;
}

@media screen and (max-width: 767px) {
	.tm-chip {
		font-size: 15px;
	}
}
</style>

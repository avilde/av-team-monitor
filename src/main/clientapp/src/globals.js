const URL = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080/',
	URL_QC = `${URL}getAllDefects`,
	URL_JIRA = `${URL}getAllJiraItems`,
	// constants
	VUETIFY_COLORS = {
		red: {
			base: '#F44336',
			'lighten-5': '#FFEBEE',
			'lighten-4': '#FFCDD2',
			'lighten-3': '#EF9A9A',
			'lighten-2': '#E57373',
			'lighten-1': '#EF5350',
			'darken-1': '#E53935',
			'darken-2': '#D32F2F',
			'darken-3': '#C62828',
			'darken-4': '#B71C1C',
			'accent-1': '#FF8A80',
			'accent-2': '#FF5252',
			'accent-3': '#FF1744',
			'accent-4': '#D50000'
		},

		pink: {
			base: '#e91e63',
			'lighten-5': '#fce4ec',
			'lighten-4': '#f8bbd0',
			'lighten-3': '#f48fb1',
			'lighten-2': '#f06292',
			'lighten-1': '#ec407a',
			'darken-1': '#d81b60',
			'darken-2': '#c2185b',
			'darken-3': '#ad1457',
			'darken-4': '#880e4f',
			'accent-1': '#ff80ab',
			'accent-2': '#ff4081',
			'accent-3': '#f50057',
			'accent-4': '#c51162'
		},

		purple: {
			base: '#9c27b0',
			'lighten-5': '#f3e5f5',
			'lighten-4': '#e1bee7',
			'lighten-3': '#ce93d8',
			'lighten-2': '#ba68c8',
			'lighten-1': '#ab47bc',
			'darken-1': '#8e24aa',
			'darken-2': '#7b1fa2',
			'darken-3': '#6a1b9a',
			'darken-4': '#4a148c',
			'accent-1': '#ea80fc',
			'accent-2': '#e040fb',
			'accent-3': '#d500f9',
			'accent-4': '#aa00ff'
		},

		'deep-purple': {
			base: '#673ab7',
			'lighten-5': '#ede7f6',
			'lighten-4': '#d1c4e9',
			'lighten-3': '#b39ddb',
			'lighten-2': '#9575cd',
			'lighten-1': '#7e57c2',
			'darken-1': '#5e35b1',
			'darken-2': '#512da8',
			'darken-3': '#4527a0',
			'darken-4': '#311b92',
			'accent-1': '#b388ff',
			'accent-2': '#7c4dff',
			'accent-3': '#651fff',
			'accent-4': '#6200ea'
		},

		indigo: {
			base: '#3f51b5',
			'lighten-5': '#e8eaf6',
			'lighten-4': '#c5cae9',
			'lighten-3': '#9fa8da',
			'lighten-2': '#7986cb',
			'lighten-1': '#5c6bc0',
			'darken-1': '#3949ab',
			'darken-2': '#303f9f',
			'darken-3': '#283593',
			'darken-4': '#1a237e',
			'accent-1': '#8c9eff',
			'accent-2': '#536dfe',
			'accent-3': '#3d5afe',
			'accent-4': '#304ffe'
		},

		blue: {
			base: '#2196F3',
			'lighten-5': '#E3F2FD',
			'lighten-4': '#BBDEFB',
			'lighten-3': '#90CAF9',
			'lighten-2': '#64B5F6',
			'lighten-1': '#42A5F5',
			'darken-1': '#1E88E5',
			'darken-2': '#1976D2',
			'darken-3': '#1565C0',
			'darken-4': '#0D47A1',
			'accent-1': '#82B1FF',
			'accent-2': '#448AFF',
			'accent-3': '#2979FF',
			'accent-4': '#2962FF'
		},

		'light-blue': {
			base: '#03a9f4',
			'lighten-5': '#e1f5fe',
			'lighten-4': '#b3e5fc',
			'lighten-3': '#81d4fa',
			'lighten-2': '#4fc3f7',
			'lighten-1': '#29b6f6',
			'darken-1': '#039be5',
			'darken-2': '#0288d1',
			'darken-3': '#0277bd',
			'darken-4': '#01579b',
			'accent-1': '#80d8ff',
			'accent-2': '#40c4ff',
			'accent-3': '#00b0ff',
			'accent-4': '#0091ea'
		},

		cyan: {
			base: '#00bcd4',
			'lighten-5': '#e0f7fa',
			'lighten-4': '#b2ebf2',
			'lighten-3': '#80deea',
			'lighten-2': '#4dd0e1',
			'lighten-1': '#26c6da',
			'darken-1': '#00acc1',
			'darken-2': '#0097a7',
			'darken-3': '#00838f',
			'darken-4': '#006064',
			'accent-1': '#84ffff',
			'accent-2': '#18ffff',
			'accent-3': '#00e5ff',
			'accent-4': '#00b8d4'
		},

		teal: {
			base: '#009688',
			'lighten-5': '#e0f2f1',
			'lighten-4': '#b2dfdb',
			'lighten-3': '#80cbc4',
			'lighten-2': '#4db6ac',
			'lighten-1': '#26a69a',
			'darken-1': '#00897b',
			'darken-2': '#00796b',
			'darken-3': '#00695c',
			'darken-4': '#004d40',
			'accent-1': '#a7ffeb',
			'accent-2': '#64ffda',
			'accent-3': '#1de9b6',
			'accent-4': '#00bfa5'
		},

		green: {
			base: '#4CAF50',
			'lighten-5': '#E8F5E9',
			'lighten-4': '#C8E6C9',
			'lighten-3': '#A5D6A7',
			'lighten-2': '#81C784',
			'lighten-1': '#66BB6A',
			'darken-1': '#43A047',
			'darken-2': '#388E3C',
			'darken-3': '#2E7D32',
			'darken-4': '#1B5E20',
			'accent-1': '#B9F6CA',
			'accent-2': '#69F0AE',
			'accent-3': '#00E676',
			'accent-4': '#00C853'
		},

		'light-green': {
			base: '#8bc34a',
			'lighten-5': '#f1f8e9',
			'lighten-4': '#dcedc8',
			'lighten-3': '#c5e1a5',
			'lighten-2': '#aed581',
			'lighten-1': '#9ccc65',
			'darken-1': '#7cb342',
			'darken-2': '#689f38',
			'darken-3': '#558b2f',
			'darken-4': '#33691e',
			'accent-1': '#ccff90',
			'accent-2': '#b2ff59',
			'accent-3': '#76ff03',
			'accent-4': '#64dd17'
		},

		lime: {
			base: '#cddc39',
			'lighten-5': '#f9fbe7',
			'lighten-4': '#f0f4c3',
			'lighten-3': '#e6ee9c',
			'lighten-2': '#dce775',
			'lighten-1': '#d4e157',
			'darken-1': '#c0ca33',
			'darken-2': '#afb42b',
			'darken-3': '#9e9d24',
			'darken-4': '#827717',
			'accent-1': '#f4ff81',
			'accent-2': '#eeff41',
			'accent-3': '#c6ff00',
			'accent-4': '#aeea00'
		},

		yellow: {
			base: '#ffeb3b',
			'lighten-5': '#fffde7',
			'lighten-4': '#fff9c4',
			'lighten-3': '#fff59d',
			'lighten-2': '#fff176',
			'lighten-1': '#ffee58',
			'darken-1': '#fdd835',
			'darken-2': '#fbc02d',
			'darken-3': '#f9a825',
			'darken-4': '#f57f17',
			'accent-1': '#ffff8d',
			'accent-2': '#ffff00',
			'accent-3': '#ffea00',
			'accent-4': '#ffd600'
		},

		amber: {
			base: '#ffc107',
			'lighten-5': '#fff8e1',
			'lighten-4': '#ffecb3',
			'lighten-3': '#ffe082',
			'lighten-2': '#ffd54f',
			'lighten-1': '#ffca28',
			'darken-1': '#ffb300',
			'darken-2': '#ffa000',
			'darken-3': '#ff8f00',
			'darken-4': '#ff6f00',
			'accent-1': '#ffe57f',
			'accent-2': '#ffd740',
			'accent-3': '#ffc400',
			'accent-4': '#ffab00'
		},

		orange: {
			base: '#ff9800',
			'lighten-5': '#fff3e0',
			'lighten-4': '#ffe0b2',
			'lighten-3': '#ffcc80',
			'lighten-2': '#ffb74d',
			'lighten-1': '#ffa726',
			'darken-1': '#fb8c00',
			'darken-2': '#f57c00',
			'darken-3': '#ef6c00',
			'darken-4': '#e65100',
			'accent-1': '#ffd180',
			'accent-2': '#ffab40',
			'accent-3': '#ff9100',
			'accent-4': '#ff6d00'
		},

		'deep-orange': {
			base: '#ff5722',
			'lighten-5': '#fbe9e7',
			'lighten-4': '#ffccbc',
			'lighten-3': '#ffab91',
			'lighten-2': '#ff8a65',
			'lighten-1': '#ff7043',
			'darken-1': '#f4511e',
			'darken-2': '#e64a19',
			'darken-3': '#d84315',
			'darken-4': '#bf360c',
			'accent-1': '#ff9e80',
			'accent-2': '#ff6e40',
			'accent-3': '#ff3d00',
			'accent-4': '#dd2c00'
		},

		brown: {
			base: '#795548',
			'lighten-5': '#efebe9',
			'lighten-4': '#d7ccc8',
			'lighten-3': '#bcaaa4',
			'lighten-2': '#a1887f',
			'lighten-1': '#8d6e63',
			'darken-1': '#6d4c41',
			'darken-2': '#5d4037',
			'darken-3': '#4e342e',
			'darken-4': '#3e2723'
		},

		'blue-grey': {
			base: '#607d8b',
			'lighten-5': '#eceff1',
			'lighten-4': '#cfd8dc',
			'lighten-3': '#b0bec5',
			'lighten-2': '#90a4ae',
			'lighten-1': '#78909c',
			'darken-1': '#546e7a',
			'darken-2': '#455a64',
			'darken-3': '#37474f',
			'darken-4': '#263238'
		},

		grey: {
			base: '#9e9e9e',
			'lighten-5': '#fafafa',
			'lighten-4': '#f5f5f5',
			'lighten-3': '#eeeeee',
			'lighten-2': '#e0e0e0',
			'lighten-1': '#bdbdbd',
			'darken-1': '#757575',
			'darken-2': '#616161',
			'darken-3': '#424242',
			'darken-4': '#212121'
		},

		shades: {
			black: '#000000',
			white: '#FFFFFF',
			transparent: 'transparent'
		}
	},
	PRIO_ICONS = {
		blocked: 'block',
		critical: 'warning',
		major: 'arrow_upward',
		trivial: 'arrow_right_alt',
		minor: 'arrow_right_alt',
		medium: 'arrow_right_alt',
		unprioritized: 'help_outline'
	},
	TYPE_ICONS = {
		defect: {icon: 'bug', color: VUETIFY_COLORS['red']['accent-2']},
		'sub-task': {icon: 'bug', color: VUETIFY_COLORS['red']['accent-2']},
		'bug': {icon: 'bug', color: VUETIFY_COLORS['red']['accent-2']},
		epic: {icon: 'bookmark', color: VUETIFY_COLORS['green']['lighten-2']},
		story: {icon: 'bookmark', color: VUETIFY_COLORS['green']['lighten-2']},
		'solution': {icon: 'bookmark', color: VUETIFY_COLORS['green']['lighten-2']},
		'general': {icon: 'thumbtack', color: VUETIFY_COLORS['deep-orange']['accent-1']},
		task: {icon: 'thumbtack', color: VUETIFY_COLORS['deep-orange']['accent-1']},
		'test': {icon: 'vial', color: VUETIFY_COLORS['blue']['accent-2']},
		'analysis': {icon: 'search', color: VUETIFY_COLORS['blue']['accent-1']},
		'development': {icon: 'coffee', color: VUETIFY_COLORS['blue-grey']['lighten-1']},
		'documentation': {icon: 'book', color: VUETIFY_COLORS['purple']['lighten-1']},
		default: {icon: 'question-circle', color: VUETIFY_COLORS['grey']['lighten-1']}
	},
	ATTACHMENT_ICONS = {
		xls: 'file-excel',
		xlsx: 'file-excel',
		doc: 'file-word',
		docx: 'file-word',
		default: 'download'
	},
	ATTACHMENT_LABELS = {
		xls: 'MS Excel',
		xlsx: 'MS Excel',
		doc: 'MS Word',
		docx: 'MS Word'
	},
	// functions
	debounce = (func, wait, immediate) => {
		let timeout;
		return function() {
			let context = this,
				args = arguments,
				later = function() {
					timeout = null;
					if (!immediate) func.apply(context, args);
				},
				callNow = immediate && !timeout;
			clearTimeout(timeout);
			timeout = setTimeout(later, wait);
			if (callNow) func.apply(context, args);
		};
	},
	jiraToHtml = input => {
		// images
		input = input.replace(/!(.+)\|thumbnail!/gm, '(see <i>$1</i>)');
		// onenote
		input = input.replace(/^onenote:(.*)&44$/gm, '<a href="onenote:$1" title="onenote:$1" target="_blank">onenote link</a>');
		// mentions
		input = input.replace(/\[~(\w+)\]/gm, '<b>$1</b>');
		// bold
		input = input.replace(/\*(.+)\*/gm, '<b>$1</b>');
		// remove headings
		input = input.replace(/^h([0-6])\.(.*)$/gm, '');
		// external links
		input = input.replace(/^(https?:\/\/(www\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_+.~#?&//=]*))/gm, '<a href="$1" title="$1" target="_blank">link</a>');
		// ordered lists
		input = input.replace(/(\*) (.*)/gm, '• $2');
		// color
		input = input.replace(/({color:#)(([0-9a-fA-F]{2}){3}|([0-9a-fA-F]){3})}(.*)({color})/gm, '<span style="color: #$2">$5</span>');

		return input;
	},
	getRequest = async url => (await fetch(url)).json(),
	getJiraItems = async () => {
		return getRequest(URL_JIRA);
	},
	getQcItems = async () => {
		return getRequest(URL_QC);
	},
	capitalize = str => {
		return str ? str.charAt(0).toUpperCase() + str.substring(1) : '';
	};

export {VUETIFY_COLORS, PRIO_ICONS, TYPE_ICONS, ATTACHMENT_ICONS, ATTACHMENT_LABELS, URL, debounce, jiraToHtml, getRequest, getJiraItems, getQcItems, capitalize};
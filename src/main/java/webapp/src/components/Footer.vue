<template>
	<v-footer 
		:dark="settings.darkTheme"
		:light ="!settings.darkTheme"
		absolute
		color="gray darken-1"
		>
		<div
			:class="['tm-footer-content', settings.darkTheme ? 'dark' : 'light']"
		>
			<div class="tm-footer-left">Andris Vilde © 2017</div>
			<div class="tm-footer-center">
				<span 
					:class="[
						'tm-footer-team-logo',
						settings.darkTheme ? 'dark' : ''
					]"
					name="settings.name"
				>
					{{ settings.shortName }}

					<picture class="tm-footer-icon">
						<source :srcset="`../static/images/logos/app-logo-16.webp`" type="image/webp">
						<img :src="`../static/images/logos/app-logo-16.png`" alt="app logo small">
					</picture>

					<v-bottom-sheet inset>
						<fa-icon
							slot="activator"
							:color="aboutHover ? 'grey' : 'lightgrey'"
							icon="angle-up"
							size="lg"
							class="tm-about-icon mx-2"
							@mouseover="aboutHover = true"
							@mouseout="aboutHover = false"
						></fa-icon>

						<v-card tile>
							<v-card-title 
								:class="[
									!settings.darkTheme ? 'white--text' : 'black--text',
									settings.darkTheme ? settings.secondaryColor : settings.primaryColor,
									!settings.darkTheme ? 'accent-5' : 'lighten-3',
									'tm-about-header'
								]"
							><span>About</span></v-card-title>

							<v-container class="tm-about pt-4 pb-1">
								<v-layout>

									<v-layout column>
										<v-flex>
											<span class="tm-about-label">author</span>
											<v-chip>
												<v-avatar>
													<picture>
														<source :srcset="`../static/images/avatars/avatar_andrvild.webp`" type="image/webp">
														<img :src="`../static/images/avatars/avatar_andrvild.png`" alt="Andris Vilde">
													</picture>
												</v-avatar>
												Andris Vilde
											</v-chip>
										</v-flex>
									</v-layout>

									<v-layout column>
										<v-flex>
											<span class="tm-about-label">company</span>
											<v-chip>
												<v-avatar>
													<picture>
														<source :srcset="`../static/images/avatars/avatar_andrvild.webp`" type="image/webp">
														<img :src="`../static/images/avatars/avatar_andrvild.png`" alt="Andris Vilde">
													</picture>
												</v-avatar>
												Andris Vilde & Co
											</v-chip>
										</v-flex>
									</v-layout>
									
									<v-layout column class="tm-about-layout version">
										<v-flex>
											<span class="tm-about-label">version</span>
											<v-chip>
												<v-avatar :class="settings.secondaryColor">V
												</v-avatar>

												{{ settings.version }}
											</v-chip>
										</v-flex>
									</v-layout>

								</v-layout>
							</v-container>
						</v-card>
					</v-bottom-sheet>
				</span>
			</div>
			<div class="tm-footer-right">
				project <span class="tm-project">{{ settings.project }}</span> | department <span :class="['tm-project', settings.darkTheme ? '' : 'dark']">{{ settings.department }}</span>
			</div>
		</div>
	</v-footer>
</template>

<script>
export default {
	data() {
		return {
			aboutHover: false
		};
	},

	props: {
		settings: {
			type: Object
		}
	}
};
</script>

<style>
.tm-footer-content {
	text-align: center;
	color: gray;
	font-size: 12px;
	display: flex;
	width: 100%;
	height: 30px;
	line-height: 30px;
}

.tm-footer-content.light {
	color: #242424;
}

.tm-footer-content.dark {
	color: white;
}

.tm-footer-left {
	width: 33%;
	float: left;
	text-align: left;
	padding-left: 50px;
}

.tm-footer-center {
	display: flex;
	justify-content: center;
	font-size: 16px;
	width: 34%;
	font-weight: bold;
}

.tm-footer-right {
	width: 33%;
	float: right;
	text-align: right;
	padding-right: 50px;
}

.tm-project {
	display: inline;
	color: #009fdf;
	font-weight: bold;
	font-size: 12px;
}

span.tm-footer-team-logo {
	display: flex;
	height: 16px;
}

span.tm-footer-team-logo picture.tm-footer-icon {
	height: inherit;
	width: 16px;
	margin-left: 3px;
	margin-top: 2px;
}

span.tm-footer-team-logo.dark picture.tm-footer-icon {
	filter: grayscale(.9) brightness(2);
}

.tm-about-icon {
	cursor: pointer;
}

.tm-about-header {
	display: flex;
	align-items: center;
	justify-content: flex-start;
	height: 2rem;
	padding: 0 1rem;
}

.tm-about-label {
	display: block;
	font-size: .8em;
	color: darkgray;
	padding-left: 10px;
}

.bottom-sheet.dialog.bottom-sheet--inset {
	max-width: 500px;	
}

@media screen and (max-width: 830px) {
	.tm-footer-left {
		width: 90px;
		padding-left: 5px;
	}

	.tm-footer-right {
		width: 60%;
	}

	.tm-footer-center {
		width: 80px;
		order: -1;
		font-size: 12px;
	}
}

@media screen and (max-width: 480px) {
	.tm-footer-right {
		font-size: 0;
	}

	.tm-project:last-child {
		margin: 0 5px;
	}

	.tm-project:last-child::before {
		content: "|";
		color: white;
		margin-right: 5px;
		font-size: 16px;
	}

	.tm-project.dark:last-child:before {
		color: black;
	}

	.tm-about-layout.version {
		display: none;
	}
}

@media screen and (max-width: 380px) {
	.tm-footer-right {
		display: none;
	}
}
</style>

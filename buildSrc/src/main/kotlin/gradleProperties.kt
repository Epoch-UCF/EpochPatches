import org.gradle.api.Project

//region gradle.properties accessors
// gradle.properties accessors, so we don't have to do e.g. `project.properties["mod_id"]` constantly
val Project.mod_id: String
	get() = properties["mod_id"] as String
val Project.mod_name: String
	get() = properties["mod_name"] as String
val Project.mod_license: String
	get() = properties["mod_license"] as String
val Project.mod_version: String
	get() = properties["mod_version"] as String
val Project.mod_authors: String
	get() = properties["mod_authors"] as String
val Project.mod_description: String
	get() = properties["mod_description"] as String
val Project.mod_group_id: String
	get() = properties["mod_group_id"] as String
val Project.parchment_mappings_version: String
	get() = properties["parchment_mappings_version"] as String
val Project.parchment_minecraft_version: String
	get() = properties["parchment_minecraft_version"] as String
val Project.minecraft_version: String
	get() = properties["minecraft_version"] as String
val Project.minecraft_version_range: String
	get() = properties["minecraft_version_range"] as String
val Project.neo_version: String
	get() = properties["neo_version"] as String
val Project.neo_version_range: String
	get() = properties["neo_version_range"] as String
val Project.loader_version_range: String
	get() = properties["loader_version_range"] as String
//endregion
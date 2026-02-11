//region gradle.properties accessors
// gradle.properties accessors, so we don't have to do e.g. `project.properties["mod_id"]` constantly
val Project.mod_id: String by properties
val Project.mod_name: String by properties
val Project.mod_license: String by properties
val Project.mod_version: String by properties
val Project.mod_authors: String by properties
val Project.mod_description: String by properties
val Project.mod_group_id: String by properties
val Project.parchment_mappings_version: String by properties
val Project.parchment_minecraft_version: String by properties
val Project.minecraft_version: String by properties
val Project.minecraft_version_range: String by properties
val Project.neo_version: String by properties
val Project.neo_version_range: String by properties
val Project.loader_version_range: String by properties
//endregion
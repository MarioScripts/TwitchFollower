#!/usr/bin/env python
# -*- coding: utf-8 -*-

# GIMP plugin to interleave layers from two different images
# (c) Ofnuts 2012
#
#   History:
#
#   v0.0: 2012-01-30: First published version
#   v0.1: 2012-01-30: Fix image created grayscale
#                     Add Merge option
#   v0.2: 2012-01-30: Minor fixes (labels in dialog)
#   v0.3: 2012-02-07: Add single-layer mode
#   v0.4: 2012-07-09: Add 'sprite' single-layer mode
#
#   This program is free software; you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation; either version 2 of the License, or
#   (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

# This plugin is dedicated to PowerPoint presentations and their authors.
# Nothing looks more like someone taking notes than someone coding a plugin.


import os,sys
from gimpfu import *

MODES=[NORMAL_MODE, DISSOLVE_MODE, MULTIPLY_MODE, DIVIDE_MODE, SCREEN_MODE, OVERLAY_MODE, DODGE_MODE, BURN_MODE, HARDLIGHT_MODE, SOFTLIGHT_MODE, GRAIN_EXTRACT_MODE, GRAIN_MERGE_MODE, DIFFERENCE_MODE, ADDITION_MODE, SUBTRACT_MODE, DARKEN_ONLY_MODE, LIGHTEN_ONLY_MODE, HUE_MODE, SATURATION_MODE, COLOR_MODE, VALUE_MODE]
MODES_LABELS=['Normal','Dissolve','Multiply','Divide','Screen','Overlay','Dodge','Burn','Hard light','Soft light','Grain extract','Grain merge','Difference','Addition','Subtract','Darken only','Lighten only','Hue','Saturation','Color','Value']

SIZES_MISMATCH="Sizes of images don't match! %s: %dx%d, %s: %dx%d" 
LAYERS_MISMATCH="Numbers of visible layers in both images don't match! %s: %d, %s: %d" 

def copyLayers(target,imageLayers,mergeLayers,opacity,mode,applyMerge):
	target.disable_undo()
	for baseSource,overSource in reversed(zip(imageLayers,mergeLayers)):
		base= pdb.gimp_layer_new_from_drawable(baseSource,target)
		over= pdb.gimp_layer_new_from_drawable(overSource,target)
		target.add_layer(base,0)
		target.add_layer(over,0)
		over.mode=mode
		over.opacity=opacity
		if applyMerge:
			base=pdb.gimp_image_merge_down(target,over,EXPAND_AS_NECESSARY)
	target.enable_undo()
	
def interleave_multiple(image,drawable,merge,opacity,mode,applyMerge):
	try:
		if image.height!=merge.height or image.width!=merge.width:
			raise Exception(SIZES_MISMATCH % (image.name,image.width,image.height, merge.name,merge.width,merge.height))
		
		# Obtain list of layers
		imageLayers=[layer for layer in image.layers if layer.visible]
		mergeLayers=[layer for layer in merge.layers if layer.visible]
		
		imageLayersCount= len(imageLayers)
		mergeLayersCount= len(mergeLayers)
		#print imageLayersCount, mergeLayersCount
		if imageLayersCount != mergeLayersCount:
			raise Exception(LAYERS_MISMATCH % (image.name,imageLayersCount,merge.name,mergeLayersCount))
		target=pdb.gimp_image_new(image.width, image.height, RGB)
		copyLayers(target,imageLayers,mergeLayers,opacity*1.,MODES[mode],applyMerge)
		pdb.gimp_display_new(target)
			
        except Exception as e:
		print e.args[0]
		pdb.gimp_message(e.args[0])
	
def copyLayerOver(target,imageLayers,mergeLayer,opacity,mode,applyMerge):
	target.disable_undo()
	for baseSource in reversed(imageLayers):
		base= pdb.gimp_layer_new_from_drawable(baseSource,target)
		over= pdb.gimp_layer_new_from_drawable(mergeLayer,target)
		target.add_layer(base,0)
		target.add_layer(over,0)
		over.mode=mode
		over.opacity=opacity
		if applyMerge:
			base=pdb.gimp_image_merge_down(target,over,EXPAND_AS_NECESSARY)
	target.enable_undo()

def interleave_single_over(image,drawable,merge,opacity,mode,applyMerge):
	try:
		if image.height!=merge.height or image.width!=merge.width:
			raise Exception(SIZES_MISMATCH % (image.name,image.width,image.height, merge.name,merge.width,merge.height))
		
		# Obtain list of layers
		imageLayers=[layer for layer in image.layers if layer.visible]
		target=pdb.gimp_image_new(image.width, image.height, RGB)
		copyLayerOver(target,imageLayers,merge,opacity*1.,MODES[mode],applyMerge)
		pdb.gimp_display_new(target)
			
        except Exception as e:
		print e.args[0]
		pdb.gimp_message(e.args[0])
	
def copyLayerUnder(target,imageLayers,mergeLayer,opacity,mode,applyMerge):
	target.disable_undo()
	for overSource in reversed(imageLayers):
		base= pdb.gimp_layer_new_from_drawable(mergeLayer,target)
		over= pdb.gimp_layer_new_from_drawable(overSource,target)
		target.add_layer(base,0)
		target.add_layer(over,0)
		over.mode=mode
		over.opacity=opacity
		if applyMerge:
			base=pdb.gimp_image_merge_down(target,over,EXPAND_AS_NECESSARY)
	target.enable_undo()

def interleave_single_under(image,drawable,merge,opacity,mode,applyMerge):
	try:
		if image.height!=merge.height or image.width!=merge.width:
			raise Exception(SIZES_MISMATCH % (image.name,image.width,image.height, merge.name,merge.width,merge.height))
		
		# Obtain list of layers
		imageLayers=[layer for layer in image.layers if layer.visible]
		target=pdb.gimp_image_new(image.width, image.height, RGB)
		copyLayerUnder(target,imageLayers,merge,opacity*1.,MODES[mode],applyMerge)
		pdb.gimp_display_new(target)
			
        except Exception as e:
		print e.args[0]
		pdb.gimp_message(e.args[0])
	
### Registrations
whoiam='\n'+os.path.abspath(sys.argv[0])

author='Ofnuts'
copyrightYear='2012'
imageType='RGB*'
menuLocation='<Image>/Image/Interleave layers'

registrationImage=(PF_IMAGE, 'image', 'Input image', None)
registrationDrawable=(PF_DRAWABLE, 'drawable', '***', None)
registrationSourceImage=(PF_IMAGE, 'source', 'Layers source:', None)
registrationSourceLayer=(PF_DRAWABLE, 'source', 'Layer:', None)
registrationOpacity=(PF_SLIDER, 'opacity',  'Opacity:', 100, (0, 100, 1))
registrationMode=(PF_OPTION, 'mode', 'Mode:', 0, MODES_LABELS)
registrationMerge=(PF_TOGGLE, 'applyMerge', 'Merge:', 1)

    
register(
	'interleave-multiple-layers',
	'Stack over stack'+whoiam,
	'Interleave stacks of layers',
	author,author,copyrightYear,
	'Interleave stacks of layers...',
	imageType,
	[
		registrationImage,
		registrationDrawable,
		registrationSourceImage,
		registrationOpacity,
		registrationMode,
		registrationMerge
	],
	[],
	interleave_multiple,
	menu=menuLocation,
	domain=('gimp20-python', gimp.locale_directory)
)

register(
	'interleave-single-layer-over',
	'Single layer over stack (title)'+whoiam,
	'Interleave single layer (title mode)',
	author,author,copyrightYear,
	'Interleave single layer (title mode)...',
	imageType,
	[
		registrationImage,
		registrationDrawable,
		registrationSourceLayer,
		registrationOpacity,
		registrationMode,
		registrationMerge
	],
	[],
	interleave_single_over,
	menu=menuLocation,
	domain=('gimp20-python', gimp.locale_directory)
)

register(
	'interleave-single-layer-under',
	'Single layer under stack (sprite)'+whoiam,
	'Interleave single layer (sprite mode)',
	author,author,copyrightYear,
	'Interleave single layer (sprite mode)',
	imageType,
	[
		registrationImage,
		registrationDrawable,
		registrationSourceLayer,
		registrationOpacity,
		registrationMode,
		registrationMerge
	],
	[],
	interleave_single_under,
	menu=menuLocation,
	domain=('gimp20-python', gimp.locale_directory)
)

    
main()       

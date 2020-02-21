# MoLeWe

Model-based testing Leverage for Web (MoLeWe) is a tool that supports test case generation for web applications.

## Table of Contents

- [Instructions to install](#install)
- [Usage](#usage)
- [Experimental Package](#experimental-package)

## Install

- Eclipse IDE

## Usage

- Download the source code from this repository and open the project *MBTS4MA* in Eclipse IDE;
- Run the project *MBTS4MA*;

## Experimental Package

The experimental package can be found [here](http://testedesoftware.cp.utfpr.edu.br/).

In this URL you will find all the virtual machine files, participants projects and instructions to access the applications with preconfigured users.

Download the virtual machine for the application you want to use and the **package_experimental.zip** file. This file contains all the 18 participants projects.

Each project in the **package_experimental.zip** file has the following directories and files:
- **coverages-after-new-tests**: This directory contains the line coverage achieved after running the new tests;
- **coverages-before-new-tests**: This directory contains the line coverage achieved by the original project;
- **project - refactored before new tests**: This directory contains the original refactored project;
- **project - with new tests**: This directory contains the project with the new test classes and PageObject classes. Here, abstract methods are not yet concretized;
- **project - with new tests implemented**: This directory contains the project with the new test classes and PageObject methods are already concretized;
- **project X - after changes.png**: Project model AFTER the extension step;
- **project X - before changes.png**: Project model BEFORE the extension step;
- **ProjectX.mbtsma**: Model project saved by the MoLeWe tool.

Start the application's virtual machine and check which IP the machine received.

There are two scripts on the virtual machine:
- **before_testing.sh**: run before running the test suite, to clean up the line coverage collection;
- **after_testing.sh**: run after running the test suite, to calculate line coverage;

Select a project and change the IP address (according to the VM's IP) using any IDE.

Run the test project.

After running the tests, access the line coverage through the browser:
- *http://virtual-machine-ip/code-coverage-report/*

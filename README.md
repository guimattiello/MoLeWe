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

### Generating a model from a test suite

<img width="599" alt="Screen Shot 2020-02-20 at 14 12 24" src="https://user-images.githubusercontent.com/15221620/74961013-210b5700-53ec-11ea-94ed-356e3ad03304.png">

When you click in *New Web App Project*, the following screen will show up.

<img width="499" alt="Screen Shot 2020-02-20 at 14 32 22" src="https://user-images.githubusercontent.com/15221620/74982100-40b57600-5412-11ea-9fed-ac87eedd582d.png">

Filling the inputs, the tool will list the possible Page Object classes. The user must check the classes that represent them.

<img width="500" alt="Config screen" src="https://user-images.githubusercontent.com/15221620/74982130-4ca13800-5412-11ea-89ef-a2d40de71b72.png">

Click in the confirmation button. The model will be generated.

<img width="935" alt="Screen Shot 2020-02-20 at 14 47 05" src="https://user-images.githubusercontent.com/15221620/74982166-5fb40800-5412-11ea-9c27-50df3a2b7dbf.png">

### Complete Running Example

For a complete running example, [click here](https://github.com/guimattiello/MoLeWe/issues/1).

## Experimental Package

The experimental package contains the following files:

- [Projects and generated models (experimental-package.zip)](http://testedesoftware.cp.utfpr.edu.br/experimental-package.zip)
- Nine virtual machines with the web applications configured:
  - [Akaunting](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-akaunting.vdi)
  - [Attendize](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-attendize.vdi)
  - [Firefly](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-firefly.vdi)
  - [Laravel-Gymie](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-laravel-gymie.vdi)
  - [Lychee](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-lychee.vdi)
  - [Mapos](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-mapos.vdi)
  - [MediaWiki](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-mediawiki.vdi)
  - [OpenCart](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-opencart-coverage.vdi)
  - [sysPass](http://testedesoftware.cp.utfpr.edu.br/virtual-machines/testesoftware-syspass.vdi)
- [Metrics collected](http://testedesoftware.cp.utfpr.edu.br/metrics-collected.csv): contains all the metrics collected.
- [Instructions to access web applications](http://testedesoftware.cp.utfpr.edu.br/web-applications-instructions.pdf): contains instructions to access the web applications with preconfigured users.

Download the virtual machine for the application you want to use and the **experimental-package.zip** file. This file contains all the 18 participants projects. Read file [Instructions to access web applications](http://testedesoftware.cp.utfpr.edu.br/web-applications-instructions.pdf) to get credentials and further configurations.

Each project in the **experimental-package.zip** file has the following directories and files:
- **coverages-after-new-tests**: This directory contains the line coverage achieved after running the new tests;
- **coverages-before-new-tests**: This directory contains the line coverage achieved by the original project;
- **project - refactored before new tests**: This directory contains the original refactored project;
- **project - with new tests**: This directory contains the project with the new test classes and PageObject classes. Here, abstract methods are not yet concretized;
- **project - with new tests implemented**: This directory contains the project with the new test classes and PageObject methods are already concretized;
- **project X - after changes.png**: Project model AFTER the extension step;
- **project X - before changes.png**: Project model BEFORE the extension step;
- **ProjectX.mbtsma**: Model project saved by the MoLeWe tool.

Start the application's virtual machine and check which IP the machine received. The credencials to log into the VM are:
- User: root
- Password: utfpr

There are two scripts on the virtual machine:
- **/root/before_testing.sh**: run before running the test suite, to clean up the line coverage collection;
- **/root/after_testing.sh**: run after running the test suite, to calculate line coverage;

Select a project and change the IP address (according to the VM's IP) using any IDE.

Run the test project.

After running the tests, access the line coverage through the browser:
- *http://virtual-machine-ip/code-coverage-report/*

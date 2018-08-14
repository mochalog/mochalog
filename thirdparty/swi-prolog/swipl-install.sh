#!/usr/bin/env bash
#
# Copyright 2017 The Mochalog Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Ensure running as root (elevated permissions)
if [[ ${EUID} != 0 ]]; then
    echo "swipl-install must be executed as root. Terminating."
    exit 1
fi

version="$1"
if [[ -z ${version} ]]; then
    echo "No SWI-Prolog version specified."
    exit 1
fi

swipl_release_name="swipl-$version"
swipl_src_root="/usr/local/src/$swipl_release_name"
# Remove existing sources
# Should use existing in future, check if valid install?
rm -r ${swipl_src_root} 2>/dev/null

# Stable release archive available on
# www.swi-prolog.org (avoid Ubuntu PPA as this does not
# include plUnit
swipl_archive_link="http://www.swi-prolog.org/download/stable/src/$swipl_release_name.tar.gz"
echo "Downloading SWI-Prolog version $version from $swipl_archive_link ..."
swipl_archive_path="$swipl_src_root/$swipl_release_name.tar.gz"
# Fetch release archive
curl ${swipl_archive_link} --create-dirs -o ${swipl_archive_path}
if [[ $? != 0 ]]; then
    echo "Download of SWI-Prolog version $version failed. Terminating."
    exit 1
fi
# Ensure correct file permissions
chmod 755 ${swipl_src_root} ${swipl_archive_path}

echo "SWI-Prolog version $version downloaded successfully. Unpacking ..."
# Unpack release archive
swipl_srcs_dir="$swipl_src_root/unpacked-srcs"
# Extract downloaded tarball
# Sourced and modified from http://askubuntu.com/a/25348
# by user djeikyb - Licensed under MIT License
# (full text copy of license at 'thirdparty/snippets/MIT_LICENSE'
tar -xzf ${swipl_archive_path} -C ${swipl_src_root}
tar_rc=$?
# Rename to unpacked-srcs
mv "$swipl_src_root/$swipl_release_name" ${swipl_srcs_dir} > /dev/null 2>&1
mv_rc=$?
if [[ ${tar_rc} != 0 ]] || [[ ${mv_rc} != 0 ]]; then
    echo "Unpacking of version $version sources failed. Terminating."
    exit 1
fi
# Ensure correct source directory permissions
chmod 755 ${swipl_srcs_dir}

#swipl_lib_prefix="/usr/local/swi-$version"
swipl_lib_prefix="/usr/local/"
echo "Building SWI-Prolog and subpackages to $swipl_lib_prefix"
# Configure and build base SWI-Prolog library
# Following SWI-Prolog build instructions
# sourced and modified from http://www.codecompiling.net/node/137
cd ${swipl_srcs_dir}
echo "Configuring SWI-Prolog $version core ..."
./configure --prefix=${swipl_lib_prefix} --enable-shared > /dev/null 2>&1

echo "Installing SWI-Prolog $version core ..."
make > /dev/null 2>&1
make install > /dev/null 2>&1

# Configure and build SWI-Prolog packages (including JPL and plUnit)
cd packages

# Ensure library dependencies for archive and ssl packages are
# installed prior to package install
# Libraries required are libarchive-dev and libssl-dev
echo "Installing libarchive-dev (dependency of SWI-Prolog archive package) ..."
apt-get install libarchive-dev -y -q
echo "Installing libssl-dev (dependency of SWI-Prolog ssl package) ..."
apt-get install libssl-dev -y -q

# Packages to include in build
included_packages="archive clib cpp chr clpqr http pldoc plunit jpl sgml ssl utf8proc"
include_args=""
for included_package in ${included_packages}; do
    include_args="$include_args --with-$included_package"
done

# Packages to exclude from build
ignored_packages="odbc table xpce RDF semweb nlp tipc zlib protobufs windows PDT libedit readline pengines cql bdb"
ignore_args=""
for ignored_package in ${ignored_packages}; do
    ignore_args="$ignore_args --without-$ignored_package"
done

./configure ${include_args} ${ignore_args}

echo "Installing selected SWI-Prolog $version subpackages ..."
make > /dev/null 2>&1
make install > /dev/null 2>&1

echo "SWI-Prolog version $version successfully installed."
